import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class PlayerDAO {

	Scanner sc = new Scanner(System.in);
	Random ran = new Random();

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

	public void playerInput(String newid) {

		getConn();

		try {

			// 아이디 가져오기

			String id = newid;
			int cnt = 0;

			String sql2 = "select players_no from players";

			psmt = conn.prepareStatement(sql2);
			rs = psmt.executeQuery();

			while (rs.next()) {
				cnt++;
			}

			int num = cnt + 1;

			System.out.print("선수이름을 정해주세요(최대 3자) >> ");
			String name = sc.next();
			int stat = ran.nextInt(100) + 1;
			System.out.println(name + "의 능력치 : " + stat);

			PlayerVO player = new PlayerVO(num, name, stat);

			String sql3 = "insert into players values(?,?,?,?)";
			psmt = conn.prepareStatement(sql3);
			psmt.setString(1, id);
			psmt.setInt(2, player.getNum());
			psmt.setString(3, player.getName());
			psmt.setInt(4, player.getStat());
			psmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}

	}

	public void showPlayerList(String newid) {
		getConn();
		ArrayList<PlayerVO> playerList = new ArrayList<PlayerVO>();
		String id = newid;

		try {
			String sql = "select * from players where user_id = ? order by players_no";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();

			System.out.println("축하드립니다!!  "+id+"님의 팀이 결성됐습니다!!");
			int i = 0;
			
			while (rs.next()) {
				String name = rs.getString("PLAYERS_NAME");
				int stat = rs.getInt("PLAYERS_STAT");
				playerList.add(new PlayerVO(name, stat));

				System.out.println(playerList.get(i).toString());
				i++;

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}

	}

	public void enemyPick() {
		getConn();
		ArrayList<String> userList = new ArrayList<String>();

		String id = "";

		try {
			String sql = "select * from users where user_id != ?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			int i = 0;
			while (rs.next()) {
				String userid = rs.getString("user_id");
				userList.add(userid);
			}

			String enemyId = userList.get(ran.nextInt(userList.size()));

			ArrayList<PlayerVO> enemyList = new ArrayList<PlayerVO>();

			String sql2 = "select * from players where user_id = ? order by players_no";
			psmt = conn.prepareStatement(sql2);
			psmt.setString(1, enemyId);
			rs = psmt.executeQuery();

			int j = 0;
			
			System.out.println("상대방 ID : "+enemyId);
			while (rs.next()) {
				String name = rs.getString("PLAYERS_NAME");
				int stat = rs.getInt("PLAYERS_STAT");
				enemyList.add(new PlayerVO(name, stat));
				System.out.println(enemyList.get(j).toString());
				j++;

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}

	}
	

	
	
	
	
	

}
