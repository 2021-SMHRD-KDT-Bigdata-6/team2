import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userDAO {

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
			String sql = "select * from users where id = ? and pw = ?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, vo.getId());
			psmt.setString(2, vo.getPw());
			rs = psmt.executeQuery();
			if(rs.next()) {
				String id = rs.getString("id");
				String pw = rs.getString("pw");
				String team = rs.getString("team");
				String score = rs.getString("score");
				
				info = new userVO(id, pw, team, 0);
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
		
		String spl = "";
		try {
			psmt = conn.prepareStatement(spl);
			psmt.setString(1, vo.getId());
			psmt.setString(2, vo.getPw());
			psmt.setString(3, vo.getTeam());
			//psmt.setString(4, vo.get);
			cnt = psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		return cnt;
	}

}
