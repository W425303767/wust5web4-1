package com.wust5.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class MyFilterTest {

	private MyFilter filter;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private FilterChain chain;

	@Before
	public void setUp() {
		filter = new MyFilter();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		chain = mock(FilterChain.class);
		when(request.getSession()).thenReturn(session);
		when(request.getContextPath()).thenReturn("/wust5");
	}

	@Test
	public void loginPageIsNotFiltered() throws Exception {
		when(request.getRequestURI()).thenReturn("/wust5/html/login.html");

		filter.doFilter(request, response, chain);

		verify(chain).doFilter(request, response);
		verify(response, never()).sendRedirect(any(String.class));
	}

	@Test
	public void registerPagesAreNotFiltered() throws Exception {
		when(request.getRequestURI()).thenReturn("/wust5/html/Register.html");
		filter.doFilter(request, response, chain);

		when(request.getRequestURI()).thenReturn("/wust5/html/RegisterNew.html");
		filter.doFilter(request, response, chain);

		verify(chain, times(2)).doFilter(request, response);
		verify(response, never()).sendRedirect(any(String.class));
	}

	@Test
	public void anonymousRequestIsRedirectedToLogin() throws Exception {
		when(request.getRequestURI()).thenReturn("/wust5/html/index.html");
		when(session.getAttribute("username")).thenReturn(null);

		filter.doFilter(request, response, chain);

		verify(response).sendRedirect("/wust5/html/login.html");
		verifyNoInteractions(chain);
	}

	@Test
	public void authenticatedRequestPassesThrough() throws Exception {
		JSONObject user = new JSONObject();
		user.put("StuNo", "20171");
		when(request.getRequestURI()).thenReturn("/wust5/html/index.html");
		when(session.getAttribute("username")).thenReturn(user);

		filter.doFilter(request, response, chain);

		verify(chain).doFilter(request, response);
		verify(response, never()).sendRedirect(any(String.class));
	}

	@Test
	public void initReadsConfigurationAndDestroyIsSafe() throws Exception {
		FilterConfig config = mock(FilterConfig.class);

		filter.init(config);
		filter.destroy();

		verify(config).getInitParameter(null);
	}
}
