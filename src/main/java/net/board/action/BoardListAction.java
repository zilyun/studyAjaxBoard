package net.board.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.board.db.BoardBean;
import net.board.db.BoardDAO;
import net.common.action.Action;
import net.common.action.ActionForward;

public class BoardListAction implements Action {

	@Override
	public ActionForward excute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BoardDAO boarddao = new BoardDAO();
		List<BoardBean> boardlist = new ArrayList<BoardBean>();
		
		// 로그인 성공 시 파라미터 page가 없어요. 그래서 초기값이 필요합니다. 
		int page = 1; // 보여줄 page
		int limit = 10; // 한 페이지에 보여줄 게시판 목록의 수
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		System.out.println("넘어온 페이지 =" + page);
		
		// 추가
		if (request.getParameter("limit") != null) {
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		System.out.println("넘어온 limit =" + limit);
	
		// 총 리스트 수를 받아옵니다.
		int listcount = boarddao.getListCount();
		
		// 리스트를 받아옵니다.
		boardlist = boarddao.getBoardList(page, limit);
		
		/*
		 * 총 페이지 수
		 * = (DB에 저장된 총 리스트의 수 + 한 페이지에서 보여주는 리스트의 수 - 1) / 한 페이지에서 보여주는 리스트의 수
		 * 
		 * 예를 들어 한 페이지에서 보여주는 리스트의 수가 10개인 경우
		 * 예1) DB에 저장된 총 리스트의 수가 0이면 총 페이지 수는 0 페이지
		 * 예2) DB에 저장된 총 리스트의 수가 (1~10)이면 총 페이지 수는 1페이지
		 * 예3) DB에 저장된 총 리스트의 수가 (11~20)이면 총 페이지 수는 2페이지
		 * 예4) DB에 저장된 총 리스트의 수가 (21~30)이면 총 페이지 수는 3페이지
		 * */
		int maxpage = (listcount + limit - 1) / limit;
		System.out.println("총 페이지수 =" + maxpage);
		/*
		 * startpage : 현재 페이지 그룹에서 맨 처음에 표시될 페이지 수를 의미합니다.
		 * ([1], [11], [21] 등 ...) 보여줄 페이지가 30개일 경우 
		 * [1][2][3] ... [30] 까지 다 표
		 * */
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(false); // 주소 변경없이 jsp 페이지의 내용을 보여줍니다.
		forward.setPath("/board/boardlist.jsp");
		return forward;
	}

}
