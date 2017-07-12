package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class myservlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(arg0, arg1);
		if(connDB()==false)
			destroy();
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doDelete(req, resp);
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
		
		if(checkmessage(username,psw))
			out.println("登录成功");
		else 
			out.println(" error: your print username or password is wrong !");
	}

	@Override
	
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	public boolean connDB(){
		
		try{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance(); 
			System.out.println("Load the embedded driver"); 
			Connection conn = null; 
			Properties props = new Properties(); 
			//create and connect the database named helloDB 
			conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true", props);  
			conn.setAutoCommit(false); 
			
			// create a table and insert two records 
			Statement s = conn.createStatement(); 
			s.execute("drop table testtable");
			s.execute("create table testtable(place varchar(40), StuNo char(10),Psw char(20))");
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean checkmessage(String username,String password){
		
		
		String message="select * from testtable where StuNo='"+username+"' and Psw='"+password+"'";
		try { // load the driver 
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance(); 
			Connection conn = null; 
			Properties props = new Properties(); 
			//connect
			conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true", props); 
			conn.setAutoCommit(false); 
			
			
			// select message
			Statement s = conn.createStatement(); 
			
			if(s.execute(message))
			{
				s.close();  
				conn.commit(); 
				conn.close(); 
				return true;
			}
			else {
				return false;
			}
			
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
}
