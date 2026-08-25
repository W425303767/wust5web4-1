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

public class managerdataTest {

	private managerdata servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletTestSupport out;

	@Before
	public void setUp() throws Exception {
		servlet = new managerdata();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		out = new ServletTestSupport();
		when(response.getWriter()).thenReturn(out.writer());
		DerbyTestSupport.createFourColumnTable();
	}

	@Test
	public void doGetReturnsOnlyManagerRows() throws Exception {
		DerbyTestSupport.insert("housemaster", "20172001", "psw1", "2");
		DerbyTestSupport.insert("CS", "20171001", "psw2", "1");
		DerbyTestSupport.insert("logistics", "20172002", "psw3", "2");

		servlet.doGet(request, response);

		JSONArray data = new JSONObject(out.output()).getJSONArray("data");
		assertEquals(2, data.length());
		assertEquals("housemaster", data.getJSONObject(0).getString("place"));
		assertEquals("20172001", data.getJSONObject(0).getString("num"));
		assertEquals("psw1", data.getJSONObject(0).getString("psw"));
		assertEquals("20172002", data.getJSONObject(1).getString("num"));
	}

	@Test
	public void doGetReturnsEmptyDataWhenNoManagerExists() throws Exception {
		DerbyTestSupport.insert("CS", "20171001", "psw1", "1");

		servlet.doGet(request, response);

		assertEquals(0, new JSONObject(out.output()).getJSONArray("data").length());
	}

	@Test
	public void doGetSwallowsDatabaseErrors() throws Exception {
		DerbyTestSupport.dropTable();

		servlet.doGet(request, response);

		assertEquals(0, new JSONObject(out.output()).getJSONArray("data").length());
	}

	@Test
	public void doPostRendersPostPlaceholderPage() throws Exception {
		servlet.doPost(request, response);

		String body = out.output();
		assertTrue(body.contains("class servlet.managerdata"));
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
}
