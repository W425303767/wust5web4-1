package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.wust5.util.Numbers;
import com.wust5.util.Responses;
import com.wust5.util.StudentRecords;

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

		returndata messages = new returndata();
		messages.start = Numbers.parseDigits(request.getParameter("start"));
		messages.length = Numbers.parseDigits(request.getParameter("length"));
		messages.data = StudentRecords.findPageByCheckid("1", messages.start, messages.length);

		Responses.writeJsonData(out, messages.data);
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
