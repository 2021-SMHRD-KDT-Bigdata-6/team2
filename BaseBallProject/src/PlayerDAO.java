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

			System.out.println("축하드립니다!!  " + id + "님의 팀이 결성됐습니다!!");
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

	// void 바꿔서 enemyPick -> id return하도록
	public String enemyPick(String newid) {
		getConn();
		ArrayList<String> userList = new ArrayList<String>();

		String result = null;

		String id = newid;
		// String id = "";

		try {
			String sql = "select * from users where user_id != ?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			

			int i = 0;
			// int i 왜 선언?

			while (rs.next()) {
				// rs가 존재한다면
				String userid = rs.getString("user_id");
				// userid를 rs의 user_id column에서 받아와서
				userList.add(userid);
				// userList에 userid 담아주기
			}

			String enemyId = userList.get(ran.nextInt(userList.size()));
			result = enemyId;
			// 여기서 오류 발생 -> 해결
			// id 해결, userList 채워줌
			// enemyId 는, userList에서 userList길이만큼 랜덤숫자 중 하나 걸린거

			ArrayList<PlayerVO> enemyList = new ArrayList<PlayerVO>();
			// 상대방list

			String sql2 = "select * from players where user_id = ? order by players_no";
			psmt = conn.prepareStatement(sql2);
			psmt.setString(1, enemyId);
			rs = psmt.executeQuery();

			int j = 0;

			System.out.println("상대방 ID : " + enemyId);

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
		return result;

	}

	
	// 이걸 아래에 있는 game method에서 활용하고 싶은데
	// 그럼 객체가 필요하니까...?
	
	public int playerPick(String newid) {
		getConn();
		// database연결
		int playerPick = 0;

		ArrayList<Integer> playerList = new ArrayList<Integer>();

		String id = newid;
		String sql = "select players_no from players where user_id = ?";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			// resultQuery 에는 id가 같은 값들이 담길거야

			while (rs.next()) {
				// rs가 존재하는 동안
				int playerNo = rs.getInt("players_no");
				// playerNo를 받아와서
				playerList.add(playerNo);
				// playerList에 넣어주기
			}

			// playerList에서 랜덤으로 한 명 뽑아서 보여주기
			// 여기서는 숫자만 반환해주는 용도
			playerPick = playerList.get(ran.nextInt(playerList.size()));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return playerPick;
		// id다르게 입력하면 그에 맞는 playerPick
	}

	// game method
	// 일단은 String id
	public void game(String newId, String enemyId) throws ClassNotFoundException {
// thorws부분 수정하기
		getConn();
		
		String id = newId;
		String enemy = enemyId;
		
		int userStat = 0;
		int enemyStat = 0;
		
		int strike = 0;
		int hit = 0;
		int homerun = 0;
		
		int userScore = 0; // database에 저장된 user의 점수
		int userGameScore = 0;//게임에서 얻는 점수
		
		// 게임 종료 될 때 sql 활용해서 score update
		
		
		ArrayList<Integer>userPlayerList = new ArrayList<Integer>();
		
		try {
			
			// user 현재 Score 불러오기
			String userScoreSql = "SELECT * FROM users WHERE user_id = ?";
			psmt = conn.prepareStatement(userScoreSql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			// players 번호 받아오기
			
			while(rs.next()) {
				userScore = rs.getInt("user_score");
			}
			
			
			
			String sql = "SELECT * FROM players WHERE user_id = ?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			// players 번호 받아오기
			
			while(rs.next()) {
				int playerNo = rs.getInt("players_no");
				userPlayerList.add(playerNo);
			}
			// user의 playerList 만들어줌
			
			// 이제 여기서 선수 한 명 뽑기
			// 이 선수를 게임 내내 쓸거임
			
			int userPick = userPlayerList.get(ran.nextInt(userPlayerList.size()));
			System.out.println("userPick >> " + userPick);
			// userPick에는 user팀의 선수 번호가 담겨있음
			
			// 이 선수의 stat을 갖고 와야 함
			String sql2 = "SELECT * FROM players WHERE players_no = ?";
			psmt = conn.prepareStatement(sql2);
			psmt.setInt(1, userPick);
			rs = psmt.executeQuery();
			while(rs.next()) {
			userStat = rs.getInt("players_stat");
			System.out.println("userPlayerNo >> " + userPick);
			System.out.println("userStat >> " + userStat);
			}
			
			// ==== 여기는 이제 상대팀 고르는 부분 ====
			// enemyId는 받아옴 -> 이걸 활용해서 상대팀 랜덤으로 한 명 뽑아주기
			// 단, 게임이 1회씩 진행될 때마다 선수는 바뀌어야 함
			
//			enemyId = enemyPick(id);
//			int enemyNum = playerPick(enemyId);//상대방팀 선수 번호중 하나를 랜덤으로 얻기
			
			ArrayList<Integer>enemyPlayerList = new ArrayList<Integer>();
			
			String sql3 = "SELECT players_no FROM players WHERE user_id = ?";
			psmt = conn.prepareStatement(sql3);
			psmt.setString(1, enemy);
			rs = psmt.executeQuery();
			while(rs.next()) {
				int enemyPlayer = rs.getInt("players_no");
				enemyPlayerList.add(enemyPlayer);
			}
			
			int gameCnt = 1; // 1회, 2회... 쉽게 진행하기 위해 1로 값 넣엊움
			
			System.out.println("게임 시작 ! ");
			while(gameCnt < 10) { // game 9회 진행될때까지 진행
				// 게임이 진행될 때마다 상대방 선수 달라져야하니까 여기서 랜덤 선수 뽑아주기
				System.out.println("***"+gameCnt+"회 시작 ***");
				int enemyPick = enemyPlayerList.get(ran.nextInt(enemyPlayerList.size()));
				
				
				// enemyPick의 stat 저장해주기
				String sql4 = "SELECT * FROM players WHERE players_no = ?";
				psmt = conn.prepareStatement(sql4);
				psmt.setInt(1, enemyPick);
				rs = psmt.executeQuery();
				while(rs.next()) {
				enemyStat = rs.getInt("players_stat");
				System.out.println("enemyPlayerNo >> " + enemyPick);
				System.out.println("enemyStat >> " + enemyStat);
				}
				
				
				int match = userStat - enemyStat;
				System.out.println("match : "+match);
				if(match <= 10) {
					if(strike == 3) {
						System.out.println("!!삼 진 아 웃!!");
						break;
					}else if(strike==1) {
						System.out.println("strike : ●○○");
					}else if(strike==2) {
						System.out.println("strike : ●●○");
					}
					strike++;
					System.out.println("STRIKE");
				}else if(match <= 50) {
					hit++;
					userGameScore++;
					System.out.println("HIT !! 1 점 획득");
					System.out.println("현재 점수 >> " + userGameScore);
				}else{
					homerun++;
					userGameScore += 2;
					System.out.println("HOMERUN !!2 점 획득");
					System.out.println("현재 점수 >> " + userGameScore);
					
				}
				System.out.println("=== "+gameCnt + "회 종료! ===");
				gameCnt++;
				
				
			}
			System.out.println("게임이 종료됐습니다.");
			if(gameCnt==9) {
				if(strike != 3) {
					System.out.println("게임에서 승리하셨습니다.");
					System.out.println("새로운 선수 1명을 등록해주세요.");
					
					int cnt = 0;
					System.out.println("ID >> ");					
					String newid = sc.next();
					System.out.println("PW >> ");					
					String newPw = sc.next();
					System.out.println("TEAM >> ");					
					String newTeam = sc.next();

					// 근데 team은 기존 team으로 미리 등록해주면 좋지 않을까?
					// 일단은 그냥 입력받자
					// 여유되면 이 부분 수정하기 -> 기존 team넣어주도록

					// 새로운 선수등록 method로 따로 빼기
					// return 하게 해서 할까?
					
					int newStat = ran.nextInt(100)+1;
					
					String updatePlayer = "insert into users values(?,?,?,?)";
					
					psmt = conn.prepareStatement(updatePlayer);
					
					psmt.setString(1, newid);
					psmt.setString(2, newPw);
					psmt.setString(3, newTeam);
					psmt.setInt(4, newStat);
					cnt = psmt.executeUpdate();
				}
				
				
			}else {
				System.out.println("패배하셨습니다.");				
			}
			System.out.println("-- 경기결과 -- ");
			System.out.println("HIT : "+ hit +"개");
			System.out.println("HOMERUN : "+ homerun +"개");
			System.out.println("획득점수 : "+ userGameScore);
			
			System.out.println("게임이 종료됐습니다.");
			
			
			
			String updateUserScore = "UPDATE users SET user_score = ? WHERE user_id = ?";
			psmt = conn.prepareStatement(updateUserScore);
			psmt.setInt(1, userGameScore+userScore);
			psmt.setString(2, id);
			int row = psmt.executeUpdate();
			// 이거 catch 안넣어줘도 되나?
			// 계속 안들어감....
			if(row > 0) {
				System.out.println("점수 업데이트 완료");
			}else {
				System.out.println("오류발생");
			}
			
			String checkUserScore = "SELECT user_score FROM users WHERE user_id = ?";
			psmt = conn.prepareStatement(checkUserScore);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				int checkScore = rs.getInt("user_score");
				System.out.println("현재 전체 점수는 >> " + checkScore);
			}
			
			
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   

	}

}
