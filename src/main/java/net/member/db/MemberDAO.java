package net.member.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {

	private DataSource ds;

	// 생성자에서 JNDI 리소스를 참조하여 Connection 객체를 얻어옵니다.
	public MemberDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception se) {
			System.out.println("DB 연결 실패 : " + se);
		}
	}

	public int isId(String id) {
		int result = -1; // DB에 해당 id 가 없습니다.
		String select_sql = "select id from member where id = ?";

		try (Connection conn = ds.getConnection(); 
			 PreparedStatement pstmt = conn.prepareStatement(select_sql);) {
			pstmt.setString(1, id);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					result = 0; // DB에 해당 id 가 있습니다.
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	} // isId end
	
	public int isId(String id, String pass) {
		int result = -1; // DB에 해당 id 가 없습니다.
		String sql = "select id, password from member where id = ? ";
		try (Connection conn = ds.getConnection(); 
			 PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					// rs.getString(2), 두번째 스트링을 의미
					if(rs.getString(2).equals(pass)) {
						result = 1; // 아이디와 비밀번호 일치하는 경우
					} else {
						result = 0; // 비밀번호와 일치하지 않는 경우
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} // isId end
	
	public int insert(Member m) {
		int result = 0;
		String sql = " INSERT INTO member "
				   + " (id, password, name, age, gender, email) "
				   + " VALUES (?,?,?,?,?,?)";
		
		try (Connection con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, m.getId());
			pstmt.setString(2, m.getPassword());
			pstmt.setString(3, m.getName());
			pstmt.setInt(4, m.getAge());
			pstmt.setString(5, m.getGender());
			pstmt.setString(6, m.getEmail());
			result = pstmt.executeUpdate(); // 삽입 성공 시 result 는 1
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	} // insert end

} // class end
