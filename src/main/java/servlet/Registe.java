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

import org.json.JSONObject;

public class Registe extends HttpServlet {

	private static final String DB_URL = "jdbc:derby:wust5DB;create=true";

	/** Derby SQL state for "the table does not exist". */
	private static final String NO_SUCH_TABLE = "42Y55";

	/**
		 * Constructor of the object.
		 */
	
	public Registe() {
		super();
		
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		createtable();
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

		registe(request, response, "success1", true);
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

		registe(request, response, "success2", false);
	}

	/**
	 * Registers <code>num</code> accounts and answers <code>successMessage</code>
	 * once every account has been stored. A registration that could not be stored
	 * answers HTTP 500 rather than reporting success.
	 */
	private void registe(HttpServletRequest request, HttpServletResponse response, String successMessage,
			boolean student) throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();

		String year;
		String whereid;
		String checkid;
		int nownum;
		try {
			year = RequestParams.require(request, "year");
			whereid = RequestParams.require(request, "where");
			checkid = RequestParams.require(request, "checkid");
			nownum = RequestParams.requireInt(request, "num");
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		if (whereid.equals("0") || nownum == 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "miss messages");
			return;
		}

		String where = student ? studentPlace(whereid) : managerPlace(whereid);
		try {
			int nums = count();
			for (int i = 0; i < nownum; i++) {
				String stunum = year + whereid + "" + (i + 1 + nums);
				JSONObject StuNo = new JSONObject();
				StuNo.put("where", where);
				StuNo.put("StuNo", stunum);
				save(where, stunum, checkid);
				session.setAttribute("username", StuNo);
			}
		} catch (SQLException e) {
			throw new ServletException("Unable to register " + nownum + " account(s) in " + DB_URL, e);
		}

		PrintWriter out = response.getWriter();
		out.print(successMessage);
		out.flush();
	}

	private String studentPlace(String whereid) {

		if (whereid.equals("1"))
			return "CS";
		if (whereid.equals("2"))
			return "FL";
		if (whereid.equals("3"))
			return "Others";
		return "";
	}

	private String managerPlace(String whereid) {

		if (whereid.equals("1"))
			return "housemaster";
		if (whereid.equals("2"))
			return "logistics";
		return "";
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */

	public void createtable() throws ServletException {

		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			Connection conn = DriverManager.getConnection(DB_URL);
			try {
				Statement s = conn.createStatement();
				try {
					try {
						s.execute("drop table testtable");
					} catch (SQLException e) {
						// The very first start up has no table to drop yet; anything else is fatal.
						if (!NO_SUCH_TABLE.equals(e.getSQLState()))
							throw e;
					}
					s.execute("create table testtable(place varchar(40), StuNo varchar(20) ,Psw varchar(20),Checkid char)");
				} finally {
					s.close();
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			throw new ServletException("Unable to create the table 'testtable' in " + DB_URL, e);
		}
	}

	/** @return how many accounts are already stored */
	public int count() throws SQLException {

		Connection conn = DriverManager.getConnection(DB_URL);
		try {
			Statement s = conn.createStatement();
			try {
				ResultSet rs = s.executeQuery("select count(*) from testtable");
				try {
					return rs.next() ? rs.getInt(1) : 0;
				} finally {
					rs.close();
				}
			} finally {
				s.close();
			}
		} finally {
			conn.close();
		}
	}

	public void save(String where, String StuNo, String checkid) throws SQLException {
		//This code uses for saving numbers informations

		Connection conn = DriverManager.getConnection(DB_URL);
		try {
			conn.setAutoCommit(false);
			PreparedStatement s = conn.prepareStatement("insert into testtable values(?,?,?,?)");
			try {
				s.setString(1, where);
				s.setString(2, StuNo);
				s.setString(3, StuNo);
				s.setString(4, checkid);
				s.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				s.close();
			}
		} finally {
			conn.close();
		}
	}
	
	
}
