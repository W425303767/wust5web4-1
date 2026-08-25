package servlet;

import java.io.IOException;
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

public class Registe extends HttpServlet {

	public Registe() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if(createtable()==false)
			destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		String year = request.getParameter("year");
		String whereid = request.getParameter("where");
		String num = request.getParameter("num");
		String checkid = request.getParameter("checkid");

		if(year == null || whereid == null || num == null || checkid == null
				|| !year.matches("\\d{8}") || (!"1".equals(checkid) && !"2".equals(checkid)))
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "failed");
			return;
		}

		int whereValue;
		int number;
		try {
			whereValue = Integer.parseInt(whereid);
			number = Integer.parseInt(num);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "failed");
			return;
		}
		if((checkid.equals("1") && (!"1".equals(whereid) && !"2".equals(whereid) && !"3".equals(whereid)))
				|| (checkid.equals("2") && (!"1".equals(whereid) && !"2".equals(whereid)))
				|| number < 1 || number > 100)
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "failed");
			return;
		}

		String where = checkid.equals("1")
				? new String[] {"CS", "FL", "Others"}[whereValue - 1]
				: new String[] {"housemaster", "logistics"}[whereValue - 1];
		int oldnum = 0;
		try (Connection conn = DriverManager.getConnection("jdbc:derby:wust5DB;create=true");
				Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery("select count(*) from testtable")) {
			if(rs.next())
				oldnum = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "failed");
			return;
		}

		boolean flag = true;
		for(int i=0;i<number;i++)
		{
			String stunum = year+whereid+""+(i+1+oldnum);
			if(!save(where,stunum,checkid)) {
				flag = false;
				break;
			}
		}

		if(flag) {
			HttpSession session = request.getSession();
			session.setAttribute("username", year+whereid+""+(number+oldnum));
			session.setAttribute("role", checkid);
			response.getWriter().print(checkid.equals("1") ? "success1" : "success2");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("failed");
		}
	}

	public boolean createtable(){
		Connection conn = null;
		Statement s = null;
		try{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			conn=DriverManager.getConnection("jdbc:derby:wust5DB;create=true");
			s = conn.createStatement();
			s.execute("create table testtable(place varchar(40), StuNo varchar(20) ,Psw varchar(140),Checkid char)");
			return true;
		}catch(SQLException e){
			if("X0Y32".equals(e.getSQLState()))
				return true;
			e.printStackTrace();
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(null!=s)
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(null!=conn)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public boolean save(String where,String StuNo,String checkid)throws ServletException, IOException {
		try (Connection conn = DriverManager.getConnection("jdbc:derby:wust5DB;create=true");
				PreparedStatement s = conn.prepareStatement("insert into testtable values(?,?,?,?)")) {
			s.setString(1, where);
			s.setString(2, StuNo);
			s.setString(3, PasswordUtil.hash(StuNo));
			s.setString(4, checkid);
			s.executeUpdate();
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
