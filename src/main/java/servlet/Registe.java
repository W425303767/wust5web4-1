package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.*;

import com.wust5.util.Numbers;
import com.wust5.util.StudentRecords;

public class Registe extends HttpServlet {

	private static final Map<String, String> STUDENT_PLACES = new HashMap<String, String>();
	private static final Map<String, String> MANAGER_PLACES = new HashMap<String, String>();

	static {
		STUDENT_PLACES.put("1", "CS");
		STUDENT_PLACES.put("2", "FL");
		STUDENT_PLACES.put("3", "Others");

		MANAGER_PLACES.put("1", "housemaster");
		MANAGER_PLACES.put("2", "logistics");
	}

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

		register(request, response, STUDENT_PLACES, "success1");
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

		register(request, response, MANAGER_PLACES, "success2");
	}

	/**
	 * Creates the requested amount of accounts and reports the outcome.
	 *
	 * @param places the place name for each accepted "where" parameter
	 * @param successMessage the answer sent once at least one account was created
	 */
	private void register(HttpServletRequest request, HttpServletResponse response, Map<String, String> places,
			String successMessage) throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();

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
			String where = places.containsKey(whereid) ? places.get(whereid) : "";
			int nownum = Numbers.parseDigits(num);
			int nums = StudentRecords.count();

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
				out.print(successMessage);
			else
				out.print("failed");
			out.flush();
			out.close();
		}
	}

	public boolean createtable(){
		try{
			StudentRecords.createTable();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean save(String where,String StuNo,String checkid)throws ServletException, IOException {
		//This code uses for saving numbers informations
		return StudentRecords.insert(where, StuNo, checkid);
	}
	
	
}
