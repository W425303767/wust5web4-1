package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wust5.util.PasswordUtil;

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
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		String username = req.getParameter("username");
		String psw = req.getParameter("psw");
		if(username == null || username.isEmpty() || psw == null || psw.isEmpty())
		{
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "failed");
			return;
		}
		String message =checkmessage(username,psw);
		
		if(message.equals("success1")){
			HttpSession session = req.getSession();
			req.changeSessionId();
			session.setAttribute("username", username);
			session.setAttribute("role", "1");
			out.print("success1");
		}
		else if(message.equals("success2")) {
			HttpSession session = req.getSession();
			req.changeSessionId();
			session.setAttribute("username", username);
			out.print("success2");
			session.setAttribute("role", "2");
		}
		else {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			out.print("failed");
		}
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
	
	public String checkmessage(String username,String password){
		
		Connection conn = null;
		PreparedStatement s =null;
		String flag="false";
		try { 
			conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true"); 
			s = conn.prepareStatement("SELECT Psw, Checkid FROM testtable WHERE StuNo = ?");
			s.setString(1, username);
			try (ResultSet rs = s.executeQuery()) {
				if(rs.next() && PasswordUtil.verify(password, rs.getString("Psw"))) {
					if("1".equals(rs.getString("Checkid")))
						flag= "success1";
					else if("2".equals(rs.getString("Checkid")))
						flag= "success2";
				}
			}
			return flag;	

		}catch (Exception e){
			e.printStackTrace();
			return "false";
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
