package net.comment.action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.common.action.Action;
import net.common.action.ActionForward;
import net.common.db.CommentDAO;

public class CommentUpdateAction implements Action {

	@Override
	public ActionForward excute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		CommentDAO dao = new CommentDAO();
		net.common.db.Comment co = new net.common.db.Comment();
		co.setContent(request.getParameter("content"));
		System.out.println("content=" + co.getContent());
		co.setNum(Integer.parseInt(request.getParameter("num")));
		
		int ok = dao.commentsUpdate(co);
		response.getWriter().print(ok);
		return null;
	}

}
