package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wust5.util.DBUtil;
import com.wust5.util.Responses;
import com.wust5.util.StudentRecords;

import Model.returndata;

public class formdata extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public formdata() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		// Put your code here
		super.init(config);
		if(DBUtil.openDatabase()==false)
			destroy();
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
		String  Sdraw = request.getParameter("draw");
		int start =0;
		int length=0;
		int draw=0;
		
		if(!"".equals(Slength)&&Slength!=null){
			length = Integer.parseInt(Slength);
		}
		
		if(!"".equals(Sstart)&&Sstart!=null){
			start = Integer.parseInt(Sstart);
		}
		
		if(Sdraw.equals(""))
			draw=0;
		else draw=Integer.parseInt(Sdraw)+1;
		
		returndata messages = new returndata();
		messages.length=length;
		messages.start=start;
		messages.draw=draw;
		messages.data = StudentRecords.findPageByCheckid("1", start, length);

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

		Responses.writeServletInfoPage(response, this.getClass(), "POST");
	}

}
