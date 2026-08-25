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

public class myservlet extends HttpServlet {

	private static final String DB_URL = "jdbc:derby:wust5DB;create=true";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		connDB();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("UTF-8");
		String username;
		String psw;
		try {
			username = RequestParams.require(req, "username");
			psw = RequestParams.require(req, "psw");
		} catch (IllegalArgumentException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		PrintWriter out = resp.getWriter();
		//print jsom //print html 
		
		if(username.equals("12345")&&psw.equals("123456"))
		{
			out.print("success");
			out.flush();
			out.close();
		}
		else 
		{
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			out.println("<HTML>");
			out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
			out.println("  <BODY>");
			out.println("	error:your username or password may bu wrong!");
			out.println("    error: your print username is"+ username+"  password is "+psw);
			out.println("  </BODY>");
			out.println("</HTML>");
			out.flush();
			out.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		String username;
		String psw;
		try {
			username = RequestParams.require(req, "username");
			psw = RequestParams.require(req, "psw");
		} catch (IllegalArgumentException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		String message;
		try {
			message = checkmessage(username, psw);
		} catch (SQLException e) {
			throw new ServletException("Unable to check the credentials of '" + username + "' against " + DB_URL, e);
		}

		PrintWriter out = resp.getWriter();
		if(message.equals("success1")){
			out.print("success1");
		}
			
		else if(message.equals("success2"))
			out.print("success2");
		else
			out.println(" error: your print"+username+psw);
		out.flush();
	}

	@Override
	
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	public void connDB() throws ServletException {
		
		try{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance(); 
			Connection conn = DriverManager.getConnection(DB_URL);
			try {
				conn.createStatement().close();
			} finally {
				conn.close();
			}
		}catch(Exception e){
			throw new ServletException("Unable to open the database " + DB_URL, e);
		}
	}
	
	public String checkmessage(String username,String password) throws SQLException {
		
		String message=username+password;
		String flag="false";
		Connection conn = DriverManager.getConnection(DB_URL);
		try {
			Statement s = conn.createStatement();
			try {
				ResultSet rs = s.executeQuery("SELECT * FROM testtable ORDER BY StuNo");
				try {
					while(rs.next()) {
						StringBuilder builder = new StringBuilder(rs.getString(2));
						builder.append(rs.getInt(3));
						String getmessage = builder.toString();
						if(message.equals(getmessage)&&(rs.getString(4)).toString().equals("1"))
						{
							flag= "success1";
						}
						else if(message.equals(getmessage)&&(rs.getString(4)).toString().equals("2"))
						{
							flag= "success2";
						}
					}
					return flag;
				} finally {
					rs.close();
				}
			} finally {
				s.close();
			}
		} finally {
			conn.close();
		}
	}
	
}
