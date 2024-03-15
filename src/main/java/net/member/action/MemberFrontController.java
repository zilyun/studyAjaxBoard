package net.member.action;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.common.action.Action;
import net.common.action.ActionForward;

@WebServlet("*.net")
public class MemberFrontController extends jakarta.servlet.http.HttpServlet {

	private static final long serialVersionUID = 1L;


	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * RequestURI = /JSP_Template_JSTL/templatetest.net 
		 * contextPath =/JSP_Template_JSTL 
		 * command = /templatetest.net
		 */
		String RequestURI = request.getRequestURI();
		System.out.println(RequestURI);
		
		// getContextPath() : 컨텍스트 경로가 반환됩니다.
		// contextPath 는 "/JspProject"가 반환됩니다.
		String contextPath = request.getContextPath();
		System.out.println(contextPath);
		
		// RequestURI에서 컨텍스트 경로 길이 값의 인덱스 위치의 문자부터
		// 마지막 위치의 문자까지 추출합니다.
		// command 는 "/BoardList.do"를 반환합니다.
		String command = RequestURI.substring(contextPath.length());
		System.out.println(command);
		
		// 초기화
		ActionForward forward = null;
		Action action = null;
		
		switch (command) {
			case "/login.net":
				action = new MemberLoginAction();
				break;
			case "/join.net":
				action = new MemberJoinAction();
				break;
			case "/idcheck.net":
				action = new MemberIdCheckAction();
				break;
		} // switch (command)
		
		forward = action.excute(request, response);
		
		if (forward != null) {
			if (forward.isRedirect()) { // 리다이렉트 됩니다.
				response.sendRedirect(forward.getPath());
			} else { // 포워딩 됩니다. 
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		} // if (forward != null)
	} // doProcess

	// doProcess(request, response) 메서드를 구현하여 요청이 GET 방식이든
	// POST 방식으로 전송되어 오든 같은 메서드에서 요청을 처리할 수 있도록 하였습니다.
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

}
