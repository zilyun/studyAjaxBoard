package net.member.db;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/idcheck")
public class IdCheck extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		
		MemberDAO dao = new MemberDAO();
		
		// result가 0인 경우는 아이디가 존재하지 않는 경우
		// result가 -1인 경우는 아이디가 존재하는 경우
		int result = dao.isId(id);
		System.out.println(result);
		response.getWriter().print(result); // println으로 시작할 경우 숫자로 인식
	}
	
	
}
