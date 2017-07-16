package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import org.json.JSONArray;
import org.json.JSONObject;

import Model.returndata;

public class index1 extends HttpServlet {

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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		String  Sstart = request.getParameter("start");
		String  Slength =request.getParameter("length");
		
		int start =0;
		int length=0;
		for(int i=0;i<Sstart.length();i++)
			start = start*10+(Sstart.charAt(i)-'0');
		for(int i=0;i<Slength.length();i++)
			length=length*10+(Slength.charAt(i)-'0');
		
		returndata messages = new returndata();
		messages.length=length;
		messages.start=start;
		
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
		//你的数据库--JSONObject
				
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
