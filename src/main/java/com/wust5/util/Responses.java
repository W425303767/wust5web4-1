package com.wust5.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public final class Responses {

	private Responses() {
	}

	/**
	 * Writes the minimal HTML document used by the placeholder servlet methods.
	 */
	public static void writeHtmlPage(PrintWriter out, String... bodyLines) {
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		for (String line : bodyLines) {
			out.println(line);
		}
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Writes the default "This is &lt;servlet&gt;, using the &lt;method&gt; method"
	 * page.
	 */
	public static void writeServletInfoPage(HttpServletResponse response, Class<?> servletClass, String method)
			throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		writeHtmlPage(out, "    This is " + servletClass + ", using the " + method + " method");
	}

	/**
	 * Writes <code>{"data": [...]}</code> to the response.
	 */
	public static void writeJsonData(PrintWriter out, JSONArray data) {
		JSONObject returnmessage = new JSONObject();
		returnmessage.put("data", data);
		out.print(returnmessage.toString());
		out.flush();
		out.close();
	}
}
