package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class Test extends HttpServlet {

	private static final String DB_URL = "jdbc:derby:wust5DB;create=true";

	/**
		 * Constructor of the object.
		 */
	public Test() {
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

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		StringBuilder body = new StringBuilder();
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			log("Load the embedded driver");

			Properties props = new Properties();
			//create and connect the database named wust5DB
			Connection conn = DriverManager.getConnection(DB_URL, props);
			body.append("create and connect to wust5DB\n");
			try {
				Statement s = conn.createStatement();
				try {
					ResultSet rs = s.executeQuery("SELECT * FROM testtable ORDER BY StuNo");
					try {
						body.append("message is:\n");
						while(rs.next()) {
							body.append(rs.getString(2)).append(rs.getInt(3)).append('\n');
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
		} catch (Exception e) {
			throw new ServletException("Unable to list the rows of 'testtable' in " + DB_URL, e);
		}

		PrintWriter out = response.getWriter();
		out.print(body.toString());
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
		response.setCharacterEncoding("UTF-8");

		String year;
		String whereid;
		int newnum;
		try {
			year = RequestParams.require(request, "year").replace("-", "");
			whereid = RequestParams.require(request, "where");
			newnum = RequestParams.requireInt(request, "num");
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		String where="";
		if(whereid.equals("0"))
			where="计算机学院";
		if(whereid.equals("1"))
			where="其他学院";

		JSONArray  message= new JSONArray();
		try {
			for(int i=0;i<newnum;i++)
			{
				String stunum = year+whereid+""+(i+1);
				JSONObject StuNo =new JSONObject();
				StuNo.put("where",where);
				StuNo.put("StuNo",stunum);
				save(where,stunum);
				message.put(StuNo);
			}
		} catch (SQLException e) {
			throw new ServletException("Unable to save " + newnum + " row(s) into " + DB_URL, e);
		}

		PrintWriter out = response.getWriter();
		out.println(message.toString());
		out.flush();
	}

	public void save(String where,String StuNo) throws SQLException {
		//This code uses for saving numbers informations

		Connection conn = DriverManager.getConnection(DB_URL, new Properties());
		try {
			conn.setAutoCommit(false);

			//insert records
			PreparedStatement s = conn.prepareStatement("insert into testtable values(?,?,?)");
			try {
				s.setString(1, where);
				s.setString(2, StuNo);
				s.setString(3, StuNo);
				s.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				s.close();
			}
		} finally {
			conn.close();
		}
	}
	

}
