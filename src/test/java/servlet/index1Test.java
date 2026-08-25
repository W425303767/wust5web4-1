package servlet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import testsupport.DerbyTestSupport;
import testsupport.ServletTestSupport;

public class index1Test {

	private index1 servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletTestSupport out;

	@Before
	public void setUp() throws Exception {
		servlet = new index1();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		out = new ServletTestSupport();
		when(response.getWriter()).thenReturn(out.writer());
		DerbyTestSupport.createFourColumnTable();
	}

	@Test
	public void doGetSerialisesRowsWithCheckidOne() throws Exception {
		DerbyTestSupport.insert("CS", "20171001", "psw1", "1");
		DerbyTestSupport.insert("FL", "20171002", "psw2", "1");
		when(request.getParameter("start")).thenReturn("0");
		when(request.getParameter("length")).thenReturn("10");

		servlet.doGet(request, response);

		JSONArray data = new JSONObject(out.output()).getJSONArray("data");
		assertEquals(2, data.length());
		assertEquals("CS", data.getJSONObject(0).getString("place"));
		assertEquals("20171001", data.getJSONObject(0).getString("num"));
		assertEquals("psw1", data.getJSONObject(0).getString("psw"));
		assertEquals("FL", data.getJSONObject(1).getString("place"));
	}

	@Test
	public void doGetStopsAtTheFirstRowThatIsNotCheckidOne() throws Exception {
		DerbyTestSupport.insert("CS", "20171001", "psw1", "1");
		DerbyTestSupport.insert("FL", "20171002", "psw2", "2");
		DerbyTestSupport.insert("CS", "20171003", "psw3", "1");
		when(request.getParameter("start")).thenReturn("0");
		when(request.getParameter("length")).thenReturn("10");

		servlet.doGet(request, response);

		JSONArray data = new JSONObject(out.output()).getJSONArray("data");
		assertEquals(1, data.length());
		assertEquals("20171001", data.getJSONObject(0).getString("num"));
	}

	@Test
	public void doGetHonoursStartAndLengthPaging() throws Exception {
		for (int i = 1; i <= 5; i++) {
			DerbyTestSupport.insert("CS", "2017100" + i, "2017100" + i, "1");
		}
		when(request.getParameter("start")).thenReturn("1");
		when(request.getParameter("length")).thenReturn("2");

		servlet.doGet(request, response);

		JSONArray data = new JSONObject(out.output()).getJSONArray("data");
		assertEquals(2, data.length());
		assertEquals("20171001", data.getJSONObject(0).getString("num"));
		assertEquals("20171002", data.getJSONObject(1).getString("num"));
	}

	@Test
	public void doGetReturnsEmptyDataWhenTableIsEmpty() throws Exception {
		when(request.getParameter("start")).thenReturn("0");
		when(request.getParameter("length")).thenReturn("10");

		servlet.doGet(request, response);

		assertEquals(0, new JSONObject(out.output()).getJSONArray("data").length());
	}

	@Test
	public void doGetSwallowsDatabaseErrors() throws Exception {
		DerbyTestSupport.dropTable();
		when(request.getParameter("start")).thenReturn("0");
		when(request.getParameter("length")).thenReturn("5");

		servlet.doGet(request, response);

		assertEquals(0, new JSONObject(out.output()).getJSONArray("data").length());
	}

	@Test
	public void doPostReturnsTheHardcodedSampleRecord() throws Exception {
		servlet.doPost(request, response);

		JSONObject data = new JSONObject(out.output()).getJSONObject("data");
		assertEquals("S1", data.getString("宿舍楼"));
		assertEquals("101", data.getString("门牌号"));
		assertEquals("wustzz", data.getString("宿舍成员"));
		assertEquals("S", data.getString("评分"));
		assertEquals("Bill", data.getString("检查员"));
		assertEquals("2017-7-12", data.getString("检查时间"));
	}

	@Test
	public void initIsANoOp() throws Exception {
		servlet.init();
	}
}
