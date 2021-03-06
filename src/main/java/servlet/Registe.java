package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.*;

public class Registe extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	
	public Registe() {
		super();
		
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		if(createtable()==false)
			destroy();
		
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
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		Connection conn = null; 
		Statement s = null;
		
		
		boolean flag=false;
		JSONArray  message= new JSONArray();
		String year =request.getParameter("year");
		String whereid = request.getParameter("where");
		String num = request.getParameter("num");
		String checkid =request.getParameter("checkid");
		
		if(whereid.equals("0")||num.equals("0")||checkid.equals(""))
		{
			out.print("miss messages");
			out.flush();
			out.close();
		}
		else
		{
			String where="";
			String oldnum="";
			int nums=0;
			int nownum=0;
			for(int i=0;i<num.length();i++)
				nownum = nownum*10+(num.charAt(i)-'0');
				
try { 
				
				conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true");  
				s = conn.createStatement(); 
				ResultSet rs =s.executeQuery("select count(*) from testtable");
				
				while(rs.next()) { 
					oldnum = rs.getString(1);
				} 
				
				for(int i=0;i<oldnum.length();i++)
					nums = nums*10+(oldnum.charAt(i)-'0');
				conn.commit(); 
			   }catch (Exception e){
				e.printStackTrace();
			}finally{
				if(null!=s)
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
			
			
			if(whereid.equals("1"))
				where="CS";
			if(whereid.equals("2"))
				where="FL";
			if(whereid.equals("3"))
				where="Others";
			
			for(int i=0;i<nownum;i++)
			{
				String stunum = year+whereid+""+(i+1+nums);
				JSONObject StuNo =new JSONObject();
				StuNo.put("where",where);
				StuNo.put("StuNo",stunum);
				message.put(StuNo);
				save(where,stunum,checkid);
				session.setAttribute("username", StuNo);
				flag=true;
			}
			
			if(flag)
			out.print("success1");
			else
			out.print("failed");
			out.flush();
			out.close();
		}
		
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
		HttpSession session = request.getSession();
		Connection conn = null; 
		Statement s = null;
		
		JSONArray  message= new JSONArray();
		boolean flag=false;
		String year =request.getParameter("year");
		String whereid = request.getParameter("where");
		String num = request.getParameter("num");
		String checkid = request.getParameter("checkid");
		
		if(whereid.equals("0")||num.equals("0")||checkid.equals(""))
		{
			out.print("miss messages");
			out.flush();
			out.close();
		}
		else
		{
			String where="";
			String oldnum="";
			int nums=0;
			int nownum=0;
			
			for(int i=0;i<num.length();i++)
				nownum = nownum*10+(num.charAt(i)-'0');
			
			
			try { 
				
				conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true");  
				s = conn.createStatement(); 
				ResultSet rs =s.executeQuery("select count(*) from testtable");
				
				while(rs.next()) { 
					oldnum = rs.getString(1);
				} 
				
				for(int i=0;i<oldnum.length();i++)
					nums = nums*10+(oldnum.charAt(i)-'0');
				conn.commit(); 
			   }catch (Exception e){
				e.printStackTrace();
			}finally{
				if(null!=s)
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
			
			if(whereid.equals("1"))
				where="housemaster";
			if(whereid.equals("2"))
				where="logistics";
			
			for(int i=0;i<nownum;i++)
			{
				String stunum = year+whereid+""+(i+1+nums);
				JSONObject StuNo =new JSONObject();
				StuNo.put("where",where);
				StuNo.put("StuNo",stunum);
				message.put(StuNo);
				save(where,stunum,checkid);
				session.setAttribute("username", StuNo);
				flag=true;
			}
			
			if(flag)
				out.print("success2");
				else
				out.print("failed");
				out.flush();
				out.close();
		}
		
		
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */

	public boolean createtable(){
		
		
		Connection conn = null; 
		Statement s = null;
		try{
			
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance(); 
		
		conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true");  
		
		s = conn.createStatement(); 
		s.execute("drop table testtable");
		s.execute("create table testtable(place varchar(40), StuNo varchar(20) ,Psw varchar(20),Checkid char)");		
		return true;
		
		}catch(Exception e){
			e.printStackTrace();
			return false;
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
		
	}
	
	public boolean save(String where,String StuNo,String checkid)throws ServletException, IOException {
		//This code uses for saving numbers informations
		
		Connection conn = null; 
		Statement s = null;
		try { 
			
			conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true");  
			
			s = conn.createStatement(); 
			s.execute("insert into testtable values('"+where+"','"+StuNo+"','"+StuNo+"','"+checkid+"')"); 
			conn.commit(); 
			
			return true;
			
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(null!=s)
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
