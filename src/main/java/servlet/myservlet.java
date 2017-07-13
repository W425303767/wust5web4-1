package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class myservlet extends HttpServlet {

	
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		if(connDB()==false)
			destroy();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setContentType("UTF-8");
		PrintWriter out = resp.getWriter();
		String username = req.getParameter("username");
		String psw = req.getParameter("psw");
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
		PrintWriter out = resp.getWriter();
		String username = req.getParameter("username");
		String psw = req.getParameter("psw");
		
		if(checkmessage(username,psw)){
			out.print("success");
		}
			
		else 
			out.println(" error: your print username or password is wrong !");
	}

	@Override
	
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	public boolean connDB(){
		
		Connection conn = null;
		Statement  s = null;
		try{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance(); 
			
			conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true");  
			
			// create a table and insert two records 
			s = conn.createStatement(); 
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=s)
				try {
					s.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(null!=conn)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return true;
	}
	
	public boolean checkmessage(String username,String password){
		
		Connection conn = null;
		Statement s =null;
		String message=username+password;
		
		try { 
			conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true"); 
			s= conn.createStatement(); 
			
			ResultSet rs = s.executeQuery( "SELECT * FROM testtable ORDER BY StuNo"); 
					while(rs.next()) { 
						String getmessage;
						StringBuilder builder = new StringBuilder(rs.getString(2)); 
						builder.append(rs.getInt(3)); 
						getmessage=builder.toString(); 
						if(message.equals(getmessage))
						{
							rs.close();
							return true;
						}
							
					} 
				
					rs.close(); 
					return false;
			
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(null !=s)
				try {
					s.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if( null != conn)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	
}
