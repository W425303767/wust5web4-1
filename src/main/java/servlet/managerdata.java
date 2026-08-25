package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import Model.returndata;

public class managerdata extends HttpServlet {

	private static final String DB_URL = "jdbc:derby:wust5DB;create=true";

	/**
		 * Constructor of the object.
		 */
	public managerdata() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		connDB();
	}

	/**
		 * The doGet method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to get.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		returndata messages = new returndata();
		try {
			Connection conn = DriverManager.getConnection(DB_URL);
			try {
				Statement s = conn.createStatement();
				try {
					ResultSet rs = s.executeQuery("SELECT * FROM testtable ORDER BY StuNo");
					try {
						while (rs.next()) {
							if (!rs.getString("checkid").contentEquals("2"))
								continue;
							JSONObject message = new JSONObject();
							message.put("place", rs.getString("place"));
							message.put("num", rs.getString("StuNo"));
							message.put("psw", rs.getString("Psw"));
							messages.data.put(message);
						}
					} finally {
						rs.close();
					}
				} finally {
					s.close();
				}
			} finally {
				conn.close();
			}
		} catch (SQLException e) {
			throw new ServletException("Unable to read manager data from " + DB_URL, e);
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSONObject returnmessage = new JSONObject();
		returnmessage.put("data", messages.data);
		PrintWriter out = response.getWriter();
		out.print(returnmessage.toString());
		out.flush();
	}

	/**
		 * The doPost method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to post.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	public void connDB() throws ServletException {

		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			Connection conn = DriverManager.getConnection(DB_URL);
			try {
				conn.createStatement().close();
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			throw new ServletException("Unable to open the database " + DB_URL, e);
		}
	}

}
