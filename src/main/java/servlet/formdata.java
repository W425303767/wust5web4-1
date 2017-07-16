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

import org.json.JSONObject;

import Model.returndata;

public class formdata extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public formdata() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		// Put your code here
		super.init(config);
		if(connDB()==false)
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
		response.setCharacterEncoding("UTF-8");
		String  Sstart = request.getParameter("start");
		String  Slength =request.getParameter("length");
		String  Sdraw = request.getParameter("draw");
		int start =0;
		int length=0;
		int draw=0;
		
		if(!"".equals(Slength)&&Slength!=null){
			length = Integer.parseInt(Slength);
		}
		
		if(!"".equals(Sstart)&&Sstart!=null){
			start = Integer.parseInt(Sstart);
		}
		
		if(Sdraw.equals(""))
			draw=0;
		else draw=Integer.parseInt(Sdraw)+1;
		
		returndata messages = new returndata();
		messages.length=length;
		messages.start=start;
		messages.draw=draw;
		Connection conn = null; 
		Statement  s = null;
		try { 
			
			conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true"); 
			s = conn.createStatement(); 
			
			// list the two records 
			ResultSet rs = s.executeQuery( 
			"SELECT * FROM testtable ORDER BY StuNo"); 
			
			for(int i=start;i<start+length;i++){
				if(rs.next())
				{
				JSONObject message = new JSONObject();
				StringBuilder builder = new StringBuilder(rs.getString("place")); 
				if(rs.getString("checkid").contentEquals("1")){
					message.put("place", builder.toString());
					builder = new StringBuilder(rs.getString("StuNo"));
					message.put("num", builder.toString());
					builder=new StringBuilder(rs.getString("Psw"));
					message.put("psw", builder.toString());
					messages.data.put(message);
				}
				else break;
			}
			
		/*	while(rs.next()) { 
				JSONObject message = new JSONObject();
				StringBuilder builder = new StringBuilder(rs.getString("place")); 
				if(rs.getString("checkid").contentEquals("1")){
					message.put("place", builder.toString());
					builder = new StringBuilder(rs.getString("StuNo"));
					message.put("num", builder.toString());
					builder=new StringBuilder(rs.getString("Psw"));
					message.put("psw", builder.toString());
					messages.data.put(message);
				}*/
				
			} 
			
			
			rs.close(); 
			s.close(); 
			conn.commit(); 
			conn.close(); 
			
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			if( null != s)
				try {
					s.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(null != conn)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		JSONObject returnmessage = new JSONObject();
		returnmessage.put("data", messages.data);
		out.print(returnmessage.toString());
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
	
}
