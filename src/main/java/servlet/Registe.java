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
		
		int nums=0;
		for(int i=0;i<num.length();i++)
			nums = nums*10+(num.charAt(i)-'0');
		year=year.replace("-", "");
		if(whereid.equals("0"))
			where="计算机学院";
		if(whereid.equals("1"))
			where="其他学院";
		
		for(int i=0;i<nums;i++)
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
		s.execute("drop table if exists 'testtable'");
		s.execute("create table testtable(place varchar(40), StuNo char(10))");
		
		return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean save(String where,String StuNo)throws ServletException, IOException {
		//This code uses for saving numbers informations
		
		String message="insert into testtable values";
		
		try { // load the driver 
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance(); 
			Connection conn = null; 
			Properties props = new Properties(); 
			//create and connect the database named helloDB 
			conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true", props); 
			conn.setAutoCommit(false); 
			
			
			// create a table and insert two records 
			Statement s = conn.createStatement(); 
		//	s.execute("create table testtable(place varchar(40), StuNo char(10))");
			s.execute("insert into testtable values('计算机学院', '2014101901')"); 
			//s.execute(message+" ('"+where+"','"+StuNo+"')");
			 
			s.close();  
			conn.commit(); 
			conn.close(); 
			return true;
			
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
}
