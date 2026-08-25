package servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import testsupport.DerbyTestSupport;
import testsupport.ServletTestSupport;

public class RegisteTest {

	private Registe servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private ServletTestSupport out;

	@Before
	public void setUp() throws Exception {
		servlet = new Registe();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		out = new ServletTestSupport();
		when(response.getWriter()).thenReturn(out.writer());
		when(request.getSession()).thenReturn(session);
		DerbyTestSupport.createFourColumnTable();
	}

	private void params(String year, String where, String num, String checkid) {
		when(request.getParameter("year")).thenReturn(year);
		when(request.getParameter("where")).thenReturn(where);
		when(request.getParameter("num")).thenReturn(num);
		when(request.getParameter("checkid")).thenReturn(checkid);
	}

	@Test
	public void createtableCreatesTheStudentTable() throws Exception {
		DerbyTestSupport.bootDatabase();

		assertTrue(servlet.createtable());
		assertEquals(0, DerbyTestSupport.countRows());
	}

	@Test
	public void createtableIsIdempotent() throws Exception {
		DerbyTestSupport.bootDatabase();

		assertTrue(servlet.createtable());
		assertTrue(servlet.createtable());
	}

	@Test
	public void createtableFailsWhenThereIsNoTableToDrop() throws Exception {
		DerbyTestSupport.dropTable();

		assertFalse(servlet.createtable());
	}

	@Test
	public void initCreatesTheStudentTable() throws Exception {
		DerbyTestSupport.bootDatabase();
		ServletConfig config = mock(ServletConfig.class);

		servlet.init(config);

		assertEquals(config, servlet.getServletConfig());
		assertEquals(0, DerbyTestSupport.countRows());
	}

	@Test
	public void saveInsertsARow() throws Exception {
		assertTrue(servlet.save("CS", "2017101", "1"));

		assertEquals(1, DerbyTestSupport.countRows());
	}

	@Test
	public void saveReportsFailureWhenTheTableIsMissing() throws Exception {
		DerbyTestSupport.dropTable();

		assertFalse(servlet.save("CS", "2017101", "1"));
	}

	@Test
	public void doGetRegistersStudentsAndRemembersThemInTheSession() throws Exception {
		params("2017", "1", "2", "1");

		servlet.doGet(request, response);

		assertEquals("success1", out.output());
		assertEquals(2, DerbyTestSupport.countRows());
		verify(session, times(2)).setAttribute(eq("username"), any());
	}

	@Test
	public void doGetContinuesNumberingFromTheExistingRows() throws Exception {
		DerbyTestSupport.insert("CS", "20171", "20171", "1");
		params("2017", "2", "1", "1");

		servlet.doGet(request, response);

		assertEquals("success1", out.output());
		assertEquals(2, DerbyTestSupport.countRows());
	}

	@Test
	public void doGetRejectsIncompleteInput() throws Exception {
		params("2017", "0", "2", "1");

		servlet.doGet(request, response);

		assertEquals("miss messages", out.output());
		assertEquals(0, DerbyTestSupport.countRows());
		verify(session, never()).setAttribute(any(String.class), any());
	}

	@Test
	public void doGetRejectsAnEmptyCheckid() throws Exception {
		params("2017", "1", "2", "");

		servlet.doGet(request, response);

		assertEquals("miss messages", out.output());
	}

	@Test
	public void doGetReportsFailureWhenNothingIsRegistered() throws Exception {
		params("2017", "1", "00", "1");

		servlet.doGet(request, response);

		assertEquals("failed", out.output());
		assertEquals(0, DerbyTestSupport.countRows());
	}

	@Test
	public void doPostRegistersManagers() throws Exception {
		params("2017", "1", "2", "2");

		servlet.doPost(request, response);

		assertEquals("success2", out.output());
		assertEquals(2, DerbyTestSupport.countRows());
		verify(session, times(2)).setAttribute(eq("username"), any());
	}

	@Test
	public void doPostRejectsIncompleteInput() throws Exception {
		params("2017", "1", "0", "2");

		servlet.doPost(request, response);

		assertEquals("miss messages", out.output());
		assertEquals(0, DerbyTestSupport.countRows());
	}
}
