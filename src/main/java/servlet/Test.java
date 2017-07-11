package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Test extends HttpServlet {

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
		PrintWriter out = response.getWriter();
		
		try { // load the driver 
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance(); 
			System.out.println("Load the embedded driver"); 
			Connection conn = null; 
			Properties props = new Properties(); 
			//create and connect the database named helloDB 
			conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true", props); 
			out.println("create and connect to wust5DB"); 
			conn.setAutoCommit(false); 
			
			// create a table and insert two records 
			Statement s = conn.createStatement(); 
			s.execute("drop table if exists 'testtable'");
			s.execute("create table testtable(place varchar(40), StuNo char(10))"); 
			out.println("Created table test-table"); 
			s.execute("insert into testtable values('计算机学院', '2014101901')"); 
			s.execute("insert into testtable values ('其他学院', '2014101902')"); 
			
			// list the two records 
			ResultSet rs = s.executeQuery( 
			"SELECT * FROM testtable ORDER BY StuNo"); 
			out.println("message is:");
			while(rs.next()) { 
				StringBuilder builder = new StringBuilder(rs.getString(1)); 
				builder.append("t"); 
				builder.append(rs.getInt(2)); 
				out.println(builder.toString()); 
				} 
			
			rs.close(); 
			s.close(); 
			out.println("Closed result set and statement"); 
			conn.commit(); 
			conn.close(); 
			out.println("Committed transaction and closed connection"); 
			
		}catch (Exception e){
			e.printStackTrace();
		}
		out.flush();
		out.close();
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
		String fname = request.getParameter("username");
		String lname = request.getParameter("psw");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    Hello"+fname+" "+lname+" "+lname);
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
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
