package com.wust5.filter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Test;

public class ServletFilterTest {

	@Test
	public void lifecycleMethodsDoNothing() throws Exception {
		ServletFilter filter = new ServletFilter();
		FilterConfig config = mock(FilterConfig.class);

		filter.init(config);
		filter.destroy();

		verifyNoInteractions(config);
	}

	@Test
	public void doFilterStopsTheChain() throws Exception {
		ServletFilter filter = new ServletFilter();
		ServletRequest request = mock(ServletRequest.class);
		ServletResponse response = mock(ServletResponse.class);
		FilterChain chain = mock(FilterChain.class);

		filter.doFilter(request, response, chain);

		verifyNoInteractions(chain, request, response);
	}
}
