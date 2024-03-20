package net.board.action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.board.db.BoardBean;
import net.board.db.BoardDAO;
import net.common.action.Action;
import net.common.action.ActionForward;

public class BoardReply implements Action {

	@Override
	public ActionForward excute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionForward forward = new ActionForward();
		BoardDAO boarddao = new BoardDAO();
		
		// 파라미터로 전달받은 답변할 글 번호를 num 변수에 저장합니다.
		int num = Integer.parseInt(request.getParameter("num"));
		
		// 글 번호 num에 해당하는 내용을 가져와서 boarddata 객체에 저장합니다.
		BoardBean boarddata = boarddao.getDetail(num);
		
		// 글 내용이 없는 경우
		if (boarddata == null) {
			System.out.println("글이 존재하지 않습니다.");
			request.setAttribute("message", "글이 존재하지 않습니다.");
			forward.setPath("error/error.jsp");
		} else {
			System.out.println("답변 페이지 이동 완료");
			// 답변 폼 페이지로 이동할 때 원본 글 내용을 보여주기 위해 
			// boarddata 객체를 request 객체에 저장합니다.
			request.setAttribute("boarddata", boarddata);
			
			// 글 답변 페이지 경로 지정합니다.
			forward.setPath("board/boardReply.jsp");
		}
		forward.setRedirect(false);
		return forward;
	}

}
