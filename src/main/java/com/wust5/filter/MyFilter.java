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

import org.json.JSONObject;

public class MyFilter implements Filter {

	public FilterConfig config;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		String urls=filterConfig.getInitParameter(null);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest hrequest = (HttpServletRequest)request;
		HttpServletResponse hresp=(HttpServletResponse) response;
		HttpSession session = hrequest.getSession();
		String url=hrequest.getRequestURI();
		
		JSONObject username=(JSONObject)session.getAttribute("username");

	/*	if (hrequest.getRequestURI() != null &&hrequest.getRequestURI().equals(logonStrings)) {// 对登录页面不进行过滤
            chain.doFilter(request, response);
            return;
        }*/
		if(url.indexOf("/login.html")>-1||url.indexOf("/Register.html")>-1||url.indexOf("/RegisterNew.html")>-1){
			chain.doFilter(hrequest, response);
		}
		else if(username==null)
			hresp.sendRedirect(hrequest.getContextPath()+"/html/login.html");
		else {
			chain.doFilter(hrequest, response);
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
