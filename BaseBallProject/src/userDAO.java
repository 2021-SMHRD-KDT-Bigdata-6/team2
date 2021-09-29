import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class userDAO {
Scanner sc = new Scanner(System.in);
	private Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;
	
	

	private void getConn() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String db_url = "jdbc:oracle:thin:@project-db-stu.ddns.net:1524:xe";
			String db_id = "cgi_5_2";
			String db_pw = "smhrd2";
			conn = DriverManager.getConnection(db_url, db_id, db_pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 로그인
	public userVO login(userVO vo) {
		userVO info = null;
		getConn();
		
		try {
			String sql = "select * from users where user_id = ? and user_pw = ?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, vo.getId());
			psmt.setString(2, vo.getPw());
			rs = psmt.executeQuery();
			// ↓이게 들어가는게 맞나요??
			if(rs.next()) {
				String id = rs.getString("id");
				String pw = rs.getString("pw");
				String team = rs.getString("team");
				int score = rs.getInt("score"); 
				
				info = new userVO(id, pw, team, score); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		
		return info;
	}

	// 회원가입
	public int signup(userVO vo) {
		int cnt = 0;
		getConn();
		
//		System.out.print("아이디를 입력하세요 >> ");
//		String id = sc.next();
//		System.out.print("비밀번호를 입력하세요 >> ");
//		String pw = sc.next();
//		System.out.print("구단명을 입력하세요 >> ");
//		String team = sc.next();
		
		//userVO vo = new userVO(id, pw, team);
		
		
		String spl = "insert into users values(?,?,?,?)";
		
		try {
			psmt = conn.prepareStatement(spl);
			psmt.setString(1, vo.getId());
			psmt.setString(2, vo.getPw());
			psmt.setString(3, vo.getTeam());
			psmt.setInt(4, 0);
			cnt = psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		

		return cnt;
	}

}
