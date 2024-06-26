package net.member.action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.common.action.Action;
import net.common.action.ActionForward;
import net.member.db.MemberDAO;


public class MemberIdCheckAction implements Action {

	@Override
	public ActionForward excute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");

		MemberDAO dao = new MemberDAO();

		// result가 0인 경우는 아이디가 존재하지 않는 경우
		// result가 -1인 경우는 아이디가 존재하는 경우
		int result = dao.isId(id);
		response.getWriter().print(result); // println으로 시작할 경우 숫자로 인식
		System.out.println(result);
		return null;
	}

}
