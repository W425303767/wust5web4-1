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

	public FilterConfig config;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hrequest = (HttpServletRequest)request;
		HttpServletResponse hresp=(HttpServletResponse) response;
		HttpSession session = hrequest.getSession();
		String path=hrequest.getRequestURI().substring(hrequest.getContextPath().length());
		boolean isPublic = path.equals("/html/login.html")
				|| path.equals("/html/Register.html")
				|| path.equals("/html/RegisterNew.html")
				|| path.equals("/servlet/myservlet")
				|| path.equals("/servlet/Registe");
		if(isPublic) {
			chain.doFilter(hrequest, response);
			return;
		}

		Object username = session.getAttribute("username");
		if(username == null) {
			if(path.startsWith("/servlet/"))
				hresp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			else
				hresp.sendRedirect(hrequest.getContextPath()+"/html/login.html");
			return;
		}

		boolean managerOnly = path.equals("/servlet/managerdata")
				|| path.contains("/pages/tables/managerdata.html")
				|| path.contains("/pages/tables/managerpage.html");
		if(managerOnly && !"2".equals(String.valueOf(session.getAttribute("role")))) {
			hresp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		chain.doFilter(hrequest, response);
	}

	@Override
	public void destroy() {
	}
}
