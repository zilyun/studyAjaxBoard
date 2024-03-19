package net.board.action;

import java.io.IOException;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.board.db.BoardBean;
import net.board.db.BoardDAO;
import net.common.action.Action;
import net.common.action.ActionForward;

public class BoardAddAction implements Action {

	@Override
	public ActionForward excute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		BoardDAO boardao = new BoardDAO();
		BoardBean boarddata = new BoardBean();
		ActionForward forward = new ActionForward();
		
		String realFolder = "";
		
		// webapp 아래에 꼭 폴더 생성하세요.
		String saveFolder = "boardupload";
		
		int fileSize = 5 * 1024 * 1024; // 업로드할 파일의 최대 사이즈 입니다. 5MB
		
		// 실제 저장 경로를 지정합니다.
		ServletContext sc = request.getServletContext();
		realFolder = sc.getRealPath(saveFolder);
		System.out.println("realFolder= " + realFolder);
		boolean result = false;
		
		try {
			MultipartRequest multi = new MultipartRequest(
										request,
										realFolder,
										fileSize,
										"UTF-8",
										new DefaultFileRenamePolicy());
			
			// BoardBean 객체에 글 등록 폼에서 입력 받은 정보들을 저장합니다.
			boarddata.setBoard_name(multi.getParameter("board_name"));
			boarddata.setBoard_pass(multi.getParameter("board_pass"));
			boarddata.setBoard_subject(multi.getParameter("board_subject"));
			boarddata.setBoard_content(multi.getParameter("board_content"));
			
			// 시스템 상에 업로드된 실제 파일명을 열어 옵니다.
			String filename = multi.getFilesystemName("board_file");
			boarddata.setBoard_file(filename);
			System.out.println(filename);
			
			// 글 등록 처리를 위해 DAO의 boardInsert() 메서드를 호출합니다.
			// 글 등록 폼에서 입력한 정보가 저장되어 있는 boarddata 객체를 전달합니다.
			result = boardao.boardInsert(boarddata);
			
			// 글 등록에 실패할 경우 false를 반환합니다.
			if (result == false) {
				System.out.println("게시판 등록 실패");
				forward.setPath("error/error.jsp");
				request.setAttribute("message", "게시판 등록 실패입니다.");
				forward.setRedirect(false);
				return forward;
			} 
			System.out.println("게시판 등록 완료");
			
			// 글 등록이 완료되면 글 목록을 보여주기 위해 "BoardList.bo"로 이동합니다.
			// Redirect 여부를 true로 설정합니다.
			forward.setRedirect(true);
			forward.setPath("BoardList.bo"); // 이동할 경로를 지정합니다.
			return forward;
		} catch (Exception ex) {
			ex.printStackTrace();
			forward.setPath("error/error.jsp");
			request.setAttribute("message", "게시판 업로드 실패입니다.");
			forward.setRedirect(false);
			return forward;
		}
	}

}
