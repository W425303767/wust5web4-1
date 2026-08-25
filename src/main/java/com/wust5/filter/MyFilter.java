package com.wust5.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MyFilter implements Filter {

	private static final String LOGIN_PAGE = "/html/login.html";

	public FilterConfig config;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		config = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hrequest = (HttpServletRequest)request;
		HttpServletResponse hresp=(HttpServletResponse) response;
		HttpSession session = hrequest.getSession();
		String url=hrequest.getRequestURI();

		if(url!=null&&(url.indexOf("/login.html")>-1||url.indexOf("/Register.html")>-1||url.indexOf("/RegisterNew.html")>-1)){
			chain.doFilter(hrequest, response);
		}
		else if(session.getAttribute("username")==null)
			hresp.sendRedirect(hrequest.getContextPath()+LOGIN_PAGE);
		else {
			chain.doFilter(hrequest, response);
		}
		
	}

	@Override
	public void destroy() {
		config = null;
	}

}
