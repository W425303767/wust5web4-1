package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import org.json.JSONArray;
import org.json.JSONObject;

import Model.Course;
import Model.CourseDao;
import Model.dormitory;
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
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		/*dormitory  message = new dormitory();
		message.buildnum = "S1";
		message.housenum="401";
		message.grades="80";
		message.members="BILL GATES";
		message.cherker="mark";
		message.time="2017-07-12";
		
		
		returndata information = new returndata();
		information.data.add(message);
		
		JSONObject aaa = new JSONObject();
		aaa.put("data", information.data);
		
		out.println(aaa.toString());*/
		
		/*ArrayList<Course> data=new ArrayList<Course>();
		data=CourseDao.GetCourses();
		out.println(CourseDao.ToJON(data, Course.BeanProperty, "data").toString());

		*/
		JSONObject a = new JSONObject();
		a.put("buildnum", "1");
		a.put("housenum", "1");
		a.put("grades","1");
		a.put("members","1");
		a.put("cherker","1");
		a.put("time","1");
		
		JSONObject ab= new JSONObject();
		ab.put("buildnum", "1");
		ab.put("housenum", "1");
		ab.put("grades","1");
		ab.put("members","1");
		ab.put("cherker","1");
		ab.put("time","1");
		
		returndata information = new returndata();
		information.data.put(ab);
		information.data.put(a);
		JSONObject aaa = new JSONObject();
		aaa.put("data", information.data);
		out.println(aaa.toString());
		
		
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
