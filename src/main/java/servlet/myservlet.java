package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wust5.util.DBUtil;
import com.wust5.util.Responses;

public class myservlet extends HttpServlet {

	
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		if(DBUtil.openDatabase()==false)
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
			Responses.writeHtmlPage(out,
					"	error:your username or password may bu wrong!",
					"    error: your print username is"+ username+"  password is "+psw);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		String username = req.getParameter("username");
		String psw = req.getParameter("psw");
		String message =checkmessage(username,psw);
		
		if(message.equals("success1")){
			out.print("success1");
		}
			
		else if(message.equals("success2"))
			out.print("success2");
		else
			out.println(" error: your print"+username+psw);
	}

	@Override
	
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	public String checkmessage(String username,String password){
		
		Connection conn = null;
		Statement s =null;
		String message=username+password;
		String flag="false";
		try { 
			conn=DBUtil.getConnection();
			s= conn.createStatement(); 
			
			ResultSet rs = s.executeQuery( "SELECT * FROM testtable ORDER BY StuNo"); 
					while(rs.next()) { 
						String getmessage;
						StringBuilder builder = new StringBuilder(rs.getString(2)); 
						builder.append(rs.getInt(3));
						getmessage=builder.toString(); 
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

		}catch (Exception e){
			e.printStackTrace();
			return "false";
		}finally{
			DBUtil.closeQuietly(s);
			DBUtil.closeQuietly(conn);
		}
		
	}
	
}
