package servlet;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import testsupport.ServletTestSupport;

public class rechargeTest {

	private recharge servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletTestSupport out;

	@Before
	public void setUp() throws Exception {
		servlet = new recharge();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		out = new ServletTestSupport();
		when(response.getWriter()).thenReturn(out.writer());
	}

	@Test
	public void doGetRendersGetPlaceholderPage() throws Exception {
		servlet.doGet(request, response);

		String body = out.output();
		verify(response).setContentType("text/html");
		assertTrue(body.contains("<HTML>"));
		assertTrue(body.contains("class servlet.recharge"));
		assertTrue(body.contains("using the GET method"));
	}

	@Test
	public void doPostRendersPostPlaceholderPage() throws Exception {
		servlet.doPost(request, response);

		String body = out.output();
		assertTrue(body.contains("class servlet.recharge"));
		assertTrue(body.contains("using the POST method"));
	}

	@Test
	public void initIsANoOp() throws Exception {
		servlet.init();
	}
}
