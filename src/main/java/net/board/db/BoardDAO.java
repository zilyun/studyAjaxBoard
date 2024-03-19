package net.board.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

// SQL과 관련된 객체와 List 객체, JNDI 관련 객체를 사용하기 위해 import 합니다.
public class BoardDAO {

	private DataSource ds;

	// 생성자에서 JNDI 리소스를 참조하여 Connection 객체를 얻어옵니다.
	public BoardDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception ex) {
			System.out.println("DB 연결 실패 : " + ex);
		}
	}

	// 글의 갯수 구하기
	public int getListCount() {
		String sql = "select count(*) from board";
		int x = 0;
		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql);) {

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					x = rs.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("getListCount() 에러: " + ex);
		}
		return x;
	} // getListCount() end

	// 글 목록 보기
	public List<BoardBean> getBoardList(int page, int limit) {

		// page : 페이지
		// limit : 페이지 당 목록의 수
		// board_re_ref desc, board_re_seq asc에 의해 정렬한 것을
		// 조건절에 맞는 rnum의 범위 만큼 가져오는 쿼리문입니다.

		String board_list_sql = "select * " + " from (select rownum rnum, j.* "
				+ "		from (select board.*, nvl(cnt,0) cnt "
				+ "			  from board left outer join (select comment_board_num,count(*) cnt "
				+ "										  from comm"
				+ "										  group by comment_board_num)"
				+ "			  on board_num=comment_board_num" + "			  order by BOARD_RE_REF desc,"
				+ "			  BOARD_RE_SEQ asc) j " + "		where rownum <= ? " + "		) "
				+ " where rnum>=? and rnum<=?";

		List<BoardBean> list = new ArrayList<BoardBean>();
		// 한 페이지당 10개씩 목록인 경우 1페이지, 2페이지, 3페이지, 4페이지 ...
		int startrow = (page - 1) * limit + 1; // 읽기 시작할 row 번호(1 11 21 31 ...)
		int endrow = startrow + limit - 1; // 읽을 마지막 row 번호(10 20 30 40 ...)

		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(board_list_sql);) {
			pstmt.setInt(1, endrow);
			pstmt.setInt(2, startrow);
			pstmt.setInt(3, endrow);

			try (ResultSet rs = pstmt.executeQuery()) {

				// DB에서 가져온 데이터를 BoardBean에 담습니다.
				while (rs.next()) {
					BoardBean board = new BoardBean();
					board.setBoard_num(rs.getInt("BOARD_NUM"));
					board.setBoard_name(rs.getString("BOARD_NAME"));
					board.setBoard_subject(rs.getString("BOARD_SUBJECT"));
					board.setBoard_content(rs.getString("BOARD_CONTENT"));
					board.setBoard_file(rs.getString("BoARD_FILE"));
					board.setBoard_re_ref(rs.getInt("BOARD_RE_REF"));
					board.setBoard_re_lev(rs.getInt("BOARD_RE_LEV"));
					board.setBoard_re_seq(rs.getInt("BOARD_RE_SEQ"));
					board.setBoard_readcount(rs.getInt("BOARD_READCOUNT"));
					board.setBoard_date(rs.getString("BOARD_DATE"));
					board.setCnt(rs.getInt("cnt"));
					list.add(board); // 값을 담은 객체를 리스트에 저장힙니다.
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("getBoardList() 에러 : " + ex);
		}
		return list;
	} // getBoardList() 메서드 end

	// 글 등록하기
	public boolean boardInsert(BoardBean board) {
		int result = 0;
		String max_sql = "(select nvl(max(board_num),0)+1 from board)";

		// 원문글의 BOARD_RE_REF 필드는 자신의 글번호 입니다.
		String sql = "insert into board " + "(BOARD_NUM, BOARD_NAME, BOARD_PASS, BOARD_SUBJECT, "
				+ " BOARD_CONTENT, BOARD_FILE, BOARD_RE_REF, " + " BOARD_RE_LEV, BOARD_RE_SEQ, BOARD_READCOUNT)"
				+ " values(" + max_sql + ",?,?,?," 
				+ " 		  ?,?," + max_sql + "," 
				+ " 		  ?,?,?)";
		
		try (Connection con = ds.getConnection(); 
			 PreparedStatement pstmt = con.prepareStatement(sql);) {
			
			// 새로운 글을 등록하는 부분입니다.
			pstmt.setString(1, board.getBoard_name());
			pstmt.setString(2, board.getBoard_pass());
			pstmt.setString(3, board.getBoard_subject());
			pstmt.setString(4, board.getBoard_content());
			pstmt.setString(5, board.getBoard_file());

			// 원문의 경우 BOARD_RE_LEV, BOARD_RE_SEQ 필드 값은 0 입니다.
			pstmt.setInt(6, 0); // BOARD_RE_LEV 필드
			pstmt.setInt(7, 0); // BOARD_RE_SEQ 필드
			pstmt.setInt(8, 0); // BOARD_READCOUNT 필드

			result = pstmt.executeUpdate();
			if (result == 1) {
				System.out.println("데이터 삽입이 모두 완료되었습니다.");
				return true;
			}
			
		} catch (Exception ex) {
			System.out.println("boardInsert() 에러: " + ex);
			ex.printStackTrace();
		}
		return false;
	} // boardInsert() 메서드 end

	public BoardBean getDetail(int num) {
		BoardBean board = null;
		String sql = "select * from board where BOARD_NUM = ?";
		
		try (Connection con = ds.getConnection(); 
			PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setInt(1, num);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					board = new BoardBean();
					board.setBoard_num(rs.getInt("BOARD_NUM"));
					board.setBoard_name(rs.getString("BOARD_NAME"));
					board.setBoard_subject(rs.getString("BOARD_SUBJECT"));
					board.setBoard_content(rs.getString("BOARD_CONTENT"));
					board.setBoard_file(rs.getString("BOARD_FILE"));
					board.setBoard_re_ref(rs.getInt("BOARD_RE_REF"));
					board.setBoard_re_lev(rs.getInt("BOARD_RE_LEV"));
					board.setBoard_re_seq(rs.getInt("BOARD_RE_SEQ"));
					board.setBoard_readcount(rs.getInt("BOARD_READCOUNT"));
					board.setBoard_date(rs.getString("BOARD_DATE"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			System.out.println("getDetail() 에러: " + ex);
		}
		return board;
	} // getDetail() 메서드 end
}
