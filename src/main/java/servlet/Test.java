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

import org.json.JSONArray;
import org.json.JSONObject;

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
			s.execute("drop table testtable");
			s.execute("create table testtable(place varchar(40), StuNo char(10),Psw char(20))"); 
			
			// list the two records 
			ResultSet rs = s.executeQuery( 
			"SELECT * FROM testtable ORDER BY StuNo"); 
			out.println("message is:");
			while(rs.next()) { 
				StringBuilder builder = new StringBuilder(rs.getString(1)); 
				builder.append("\t"); 
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

	public boolean save(String where,String StuNo)throws ServletException, IOException {
		//This code uses for saving numbers informations
		
		String message = "insert into testtable values";
		try { // load the driver 
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance(); 
			Connection conn = null; 
			Properties props = new Properties(); 
			//create and connect the database named wust5DB 
			conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true", props); 
			conn.setAutoCommit(false); 
			
			//insert records 
			Statement s = conn.createStatement(); 
			s.execute(message+"('"+where+"','"+StuNo+"','"+StuNo+"')"); 
			 
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
