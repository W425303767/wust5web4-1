package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import Model.returndata;

public class index1 extends HttpServlet {

	private static final String DB_URL = "jdbc:derby:wust5DB;create=true";

	/**
		 * Constructor of the object.
		 */
	public index1() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
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

		int start;
		int length;
		try {
			start = RequestParams.requireInt(request, "start");
			length = RequestParams.requireInt(request, "length");
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		returndata messages = new returndata();
		messages.length = length;
		messages.start = start;

		try {
			Connection conn = DriverManager.getConnection(DB_URL);
			try {
				Statement s = conn.createStatement();
				try {
					ResultSet rs = s.executeQuery("SELECT * FROM testtable ORDER BY StuNo");
					try {
						for (int i = start; i < start + length; i++) {
							if (!rs.next())
								break;
							if (!rs.getString("checkid").contentEquals("1"))
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
			throw new ServletException("Unable to read index data from " + DB_URL, e);
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
		
		
		JSONObject data = new JSONObject();
		data.put("宿舍楼", "S1");
		data.put("门牌号","101");
		data.put("宿舍成员", "wustzz");
		data.put("评分", "S");
		data.put("检查员", "Bill");
		data.put("检查时间", "2017-7-12");
		
		JSONObject message = new JSONObject();
		message.put("data",data);
		
		out.println(message);
		out.flush();
		out.close();
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */
	public void init() throws ServletException {
		// Put your code here
	}

}
