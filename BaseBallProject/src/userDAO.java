import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class userDAO {

	private Connection conn;

	private void getConn() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String db_url = "jdbc:oracle:thin:@localhost:1521:xe";
			String db_id = "hr";
			String db_pw = "hr";
			conn = DriverManager.getConnection(db_url, db_id, db_pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void close() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// �α���
	public userVO login(userVO vo) {
		userVO info = null;
		getConn();

		return info;
	}

	// ȸ������
	public int signup(userVO vo) {
		int cnt = 0;

		getConn();

		return cnt;
	}

}
