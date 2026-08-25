package servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import testsupport.DerbyTestSupport;
import testsupport.ServletTestSupport;

public class myservletTest {

	private myservlet servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletTestSupport out;

	@Before
	public void setUp() throws Exception {
		servlet = new myservlet();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		out = new ServletTestSupport();
		when(response.getWriter()).thenReturn(out.writer());
		DerbyTestSupport.createFourColumnTable();
	}

	private void credentials(String username, String psw) {
		when(request.getParameter("username")).thenReturn(username);
		when(request.getParameter("psw")).thenReturn(psw);
	}

	@Test
	public void doGetAcceptsTheHardcodedCredentials() throws Exception {
		credentials("12345", "123456");

		servlet.doGet(request, response);

		assertEquals("success", out.output());
	}

	@Test
	public void doGetRejectsWrongCredentials() throws Exception {
		credentials("12345", "wrong");

		servlet.doGet(request, response);

		String body = out.output();
		assertTrue(body.contains("your username or password may bu wrong!"));
		assertTrue(body.contains("your print username is12345"));
		assertTrue(body.contains("password is wrong"));
	}

	@Test
	public void doPostReportsSuccess1ForACheckidOneAccount() throws Exception {
		DerbyTestSupport.insert("CS", "2017", "1001", "1");
		credentials("2017", "1001");

		servlet.doPost(request, response);

		assertEquals("success1", out.output());
	}

	@Test
	public void doPostReportsSuccess2ForACheckidTwoAccount() throws Exception {
		DerbyTestSupport.insert("housemaster", "2017", "2001", "2");
		credentials("2017", "2001");

		servlet.doPost(request, response);

		assertEquals("success2", out.output());
	}

	@Test
	public void doPostReportsAnErrorForUnknownAccounts() throws Exception {
		DerbyTestSupport.insert("CS", "2017", "1001", "1");
		credentials("2017", "9999");

		servlet.doPost(request, response);

		assertTrue(out.output().contains("error: your print20179999"));
	}

	@Test
	public void checkmessageReturnsFalseWhenNoAccountMatches() throws Exception {
		DerbyTestSupport.bootDatabase();

		assertEquals("false", servlet.checkmessage("2017", "1001"));
	}

	@Test
	public void checkmessageReturnsFalseWhenTheQueryFails() throws Exception {
		DerbyTestSupport.dropTable();

		assertEquals("false", servlet.checkmessage("2017", "1001"));
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
