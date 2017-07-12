package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.*;

public class Registe extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public Registe() {
		super();
		if(createtable()==false)
			destroy();
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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
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
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		
		JSONArray  message= new JSONArray();
		String year =request.getParameter("year");
		String whereid = request.getParameter("where");
		String num = request.getParameter("num");
		String where="";
		
		int newnum=0;
		for(int i=0;i<num.length();i++)
			newnum = newnum*10+(num.charAt(i)-'0');
		year=year.replace("-", "");
		if(whereid.equals("0"))
			where="计算机学院";
		if(whereid.equals("1"))
			where="其他学院";
		
		
		for(int i=0;i<newnum;i++)
		{
			String stunum = year+whereid+""+(i+1);
			JSONObject StuNo =new JSONObject();
			StuNo.put("where",where);
			StuNo.put("StuNo",stunum);
			message.put(StuNo);
			save(where,stunum);
		}
		
		out.println(message.toString());
		out.flush();
		out.close();
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */

	public boolean createtable(){
		
		Connection conn = null; 
		Statement  s =null;
		try{
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance(); 
	
		Properties props = new Properties(); 
		//create and connect the database named helloDB 
		conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true", props);  
		conn.setAutoCommit(false); 
		
		s = conn.createStatement(); 
		s.execute("drop table testtable");
		s.execute("create table testtable(place varchar(40), StuNo char(10) ,Psw char(20))");
		System.out.println("finish create table"); 
		s.close();
		return true;
		}catch(Exception e){
			return false;
		}finally{
			if(null != s)
			{
				try {
					s.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public boolean save(String where,String StuNo)throws ServletException, IOException {
		//This code uses for saving numbers informations
		
		System.out.print("start saving !");
		
		Connection conn = null; 
		Statement s = null;
		try { // load the driver 
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance(); 
			Properties props = new Properties(); 
			//create and connect the database named wust5DB 
			conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true", props);  
			
			//insert records 
			s = conn.createStatement(); 
			s.execute("insert into testtable values('计算机学院','2014101901','2014101901')"); 
			 
			//s.close();  
			conn.commit(); 
			//conn.close(); 
			return true;
			
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(null != s){
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(null != conn){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
