package servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

import testsupport.DerbyTestSupport;
import testsupport.ServletTestSupport;

public class TestServletTest {

	private servlet.Test servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletTestSupport out;

	@Before
	public void setUp() throws Exception {
		servlet = new servlet.Test();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		out = new ServletTestSupport();
		when(response.getWriter()).thenReturn(out.writer());
		DerbyTestSupport.createThreeColumnTable();
	}

	private void params(String year, String where, String num) {
		when(request.getParameter("year")).thenReturn(year);
		when(request.getParameter("where")).thenReturn(where);
		when(request.getParameter("num")).thenReturn(num);
	}

	@Test
	public void doPostGeneratesOneStudentNumberPerRequestedSeat() throws Exception {
		params("2017-09-01", "0", "3");

		servlet.doPost(request, response);

		JSONArray message = new JSONArray(out.output());
		assertEquals(3, message.length());
		assertEquals("计算机学院", message.getJSONObject(0).getString("where"));
		assertEquals("2017090101", message.getJSONObject(0).getString("StuNo"));
		assertEquals("2017090103", message.getJSONObject(2).getString("StuNo"));
		assertEquals(3, DerbyTestSupport.countRows());
	}

	@Test
	public void doPostLabelsOtherDepartments() throws Exception {
		params("2017-09-01", "1", "1");

		servlet.doPost(request, response);

		JSONArray message = new JSONArray(out.output());
		assertEquals(1, message.length());
		assertEquals("其他学院", message.getJSONObject(0).getString("where"));
		assertEquals("2017090111", message.getJSONObject(0).getString("StuNo"));
	}

	@Test
	public void doPostLeavesTheDepartmentBlankForUnknownIds() throws Exception {
		params("2017-09-01", "9", "1");

		servlet.doPost(request, response);

		assertEquals("", new JSONArray(out.output()).getJSONObject(0).getString("where"));
	}

	@Test
	public void doPostWritesAnEmptyArrayWhenNoSeatIsRequested() throws Exception {
		params("2017-09-01", "0", "0");

		servlet.doPost(request, response);

		assertEquals(0, new JSONArray(out.output()).length());
		assertEquals(0, DerbyTestSupport.countRows());
	}

	@Test
	public void saveInsertsARow() throws Exception {
		assertTrue(servlet.save("计算机学院", "2017090101"));

		assertEquals(1, DerbyTestSupport.countRows());
	}

	@Test
	public void saveReportsFailureWhenTheTableIsMissing() throws Exception {
		DerbyTestSupport.dropTable();

		assertFalse(servlet.save("计算机学院", "2017090101"));
	}

	@Test
	public void saveReportsFailureWhenTheSchemaHasFourColumns() throws Exception {
		DerbyTestSupport.createFourColumnTable();

		assertFalse(servlet.save("CS", "2017090101"));
	}

	@Test
	public void doGetListsTheStoredRows() throws Exception {
		servlet.save("计算机学院", "2017090101");

		servlet.doGet(request, response);

		String body = out.output();
		assertTrue(body.contains("create and connect to wust5DB"));
		assertTrue(body.contains("message is:"));
		assertTrue(body.contains("2017090101"));
	}

	@Test
	public void destroyIsSafeToCall() {
		servlet.destroy();
	}
}
