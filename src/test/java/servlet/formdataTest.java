package servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import testsupport.DerbyTestSupport;
import testsupport.ServletTestSupport;

public class formdataTest {

	private formdata servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletTestSupport out;

	@Before
	public void setUp() throws Exception {
		servlet = new formdata();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		out = new ServletTestSupport();
		when(response.getWriter()).thenReturn(out.writer());
		DerbyTestSupport.createFourColumnTable();
	}

	private void params(String start, String length, String draw) {
		when(request.getParameter("start")).thenReturn(start);
		when(request.getParameter("length")).thenReturn(length);
		when(request.getParameter("draw")).thenReturn(draw);
	}

	@Test
	public void doGetSerialisesRowsWithCheckidOne() throws Exception {
		DerbyTestSupport.insert("CS", "20171001", "psw1", "1");
		DerbyTestSupport.insert("FL", "20171002", "psw2", "1");
		params("0", "10", "1");

		servlet.doGet(request, response);

		JSONArray data = new JSONObject(out.output()).getJSONArray("data");
		assertEquals(2, data.length());
		assertEquals("CS", data.getJSONObject(0).getString("place"));
		assertEquals("psw1", data.getJSONObject(0).getString("psw"));
		assertEquals("20171002", data.getJSONObject(1).getString("num"));
	}

	@Test
	public void doGetStopsAtTheFirstRowThatIsNotCheckidOne() throws Exception {
		DerbyTestSupport.insert("CS", "20171001", "psw1", "1");
		DerbyTestSupport.insert("FL", "20171002", "psw2", "2");
		DerbyTestSupport.insert("CS", "20171003", "psw3", "1");
		params("0", "10", "1");

		servlet.doGet(request, response);

		assertEquals(1, new JSONObject(out.output()).getJSONArray("data").length());
	}

	@Test
	public void doGetReadsAtMostLengthRows() throws Exception {
		for (int i = 1; i <= 5; i++) {
			DerbyTestSupport.insert("CS", "2017100" + i, "psw" + i, "1");
		}
		params("0", "3", "2");

		servlet.doGet(request, response);

		JSONArray data = new JSONObject(out.output()).getJSONArray("data");
		assertEquals(3, data.length());
		assertEquals("20171001", data.getJSONObject(0).getString("num"));
		assertEquals("20171003", data.getJSONObject(2).getString("num"));
	}

	@Test
	public void doGetAcceptsEmptyPagingParameters() throws Exception {
		DerbyTestSupport.insert("CS", "20171001", "psw1", "1");
		params("", "", "");

		servlet.doGet(request, response);

		assertEquals(0, new JSONObject(out.output()).getJSONArray("data").length());
	}

	@Test
	public void doGetSwallowsDatabaseErrors() throws Exception {
		DerbyTestSupport.dropTable();
		params("0", "5", "1");

		servlet.doGet(request, response);

		assertEquals(0, new JSONObject(out.output()).getJSONArray("data").length());
	}

	@Test
	public void doPostRendersPostPlaceholderPage() throws Exception {
		servlet.doPost(request, response);

		String body = out.output();
		assertTrue(body.contains("class servlet.formdata"));
		assertTrue(body.contains("using the POST method"));
	}

	@Test
	public void connDBReportsSuccess() {
		DerbyTestSupport.bootDatabase();

		assertTrue(servlet.connDB());
	}

	@Test
	public void initConnectsToTheDatabase() throws Exception {
		DerbyTestSupport.bootDatabase();
		ServletConfig config = mock(ServletConfig.class);

		servlet.init(config);

		assertEquals(config, servlet.getServletConfig());
	}

	@Test
	public void destroyIsSafeToCall() {
		servlet.destroy();
	}
}
