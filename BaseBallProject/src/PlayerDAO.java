import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDAO {

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int playerInput(PlayerVO vo) {
		
		int num = 0;
		String name = "";
		int stat = 0;
		
		PlayerVO player = new PlayerVO(num,name,stat);
		
		int cnt = 0;

		getConn();

		try {
			String sql = "update players set name = ? Where employee_id = ?";
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, vo.getNum());
			psmt.setString(2, vo.getName());
			psmt.setInt(3, vo.getStat());
			cnt = psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}

		return cnt;

	}
	
	private int 
	
	
	
	
	

}
