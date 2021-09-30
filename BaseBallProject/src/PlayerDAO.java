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

			// ���̵� ��������

			String id = newid;
			int cnt = 0;

			String sql2 = "select players_no from players";

			psmt = conn.prepareStatement(sql2);
			rs = psmt.executeQuery();

			while (rs.next()) {
				cnt++;
			}

			int num = cnt + 1;

			System.out.print("�����̸��� �����ּ���(�ִ� 3��) >> ");
			String name = sc.next();
			int stat = ran.nextInt(100) + 1;
			System.out.println(name + "�� �ɷ�ġ : " + stat);

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

	public void showPlayerList(String newId) {
		getConn();
		ArrayList<PlayerVO> playerList = new ArrayList<PlayerVO>();
		String id = newId;

		try {

			String teamName = getTeamName(id);

			// System.out.println("���ϵ帳�ϴ�!! " + teamName + "��(��) �Ἲ�ƽ��ϴ�!!");
			System.out.println();
			System.out.println("===== " + teamName + " ���� List =====");
			// ������ ������

			int i = 0;
			String sql = "select * from players where user_id = ? order by players_no";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();

			while (rs.next()) {
				String name = rs.getString("PLAYERS_NAME");
				int stat = rs.getInt("PLAYERS_STAT");
				playerList.add(new PlayerVO(name, stat));

				System.out.println(playerList.get(i).toString());
				i++;

			}
			System.out.println();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}

	}

	// void �ٲ㼭 enemyPick -> id return�ϵ���
	public String enemyPick(String id) {
		getConn();
		ArrayList<String> userList = new ArrayList<String>();

		String result = null;

		String userId = id;

		try {
			String sql = "select * from users where user_id != ?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();

			while (rs.next()) {
				// rs�� �����Ѵٸ�
				String otherUserId = rs.getString("user_id");
				// userid�� rs�� user_id column���� �޾ƿͼ�
				userList.add(otherUserId);
				// userList�� userid ����ֱ�
			}

			String enemyId = userList.get(ran.nextInt(userList.size()));
			result = enemyId;
			// ���⼭ ���� �߻� -> �ذ�
			// id �ذ�, userList ä����
			// enemyId ��, userList���� userList���̸�ŭ �������� �� �ϳ� �ɸ���

			ArrayList<PlayerVO> myList = new ArrayList<PlayerVO>();
			ArrayList<PlayerVO> enemyList = new ArrayList<PlayerVO>();
			// ����list

			String userTeamName = getTeamName(userId);
			String enemyTeamName = getTeamName(enemyId);

			System.out.println();
			System.out.println("***** ������ ��ġ *****");
			System.out.println(userTeamName + "  VS  " + enemyTeamName);
			System.out.println();

			String sql1 = "select * from players where user_id = ? order by players_no";
			psmt = conn.prepareStatement(sql1);
			psmt.setString(1, userId);
			rs = psmt.executeQuery();

			int k = 0;

			System.out.println("===== " + userTeamName + " ���� List =====");
			while (rs.next()) {
				String name = rs.getString("PLAYERS_NAME");
				int stat = rs.getInt("PLAYERS_STAT");
				myList.add(new PlayerVO(name, stat));
				System.out.println(myList.get(k).toString());
				k++;

			}
			System.out.println();

			String sql2 = "select * from players where user_id = ? order by players_no";
			psmt = conn.prepareStatement(sql2);
			psmt.setString(1, enemyId);
			rs = psmt.executeQuery();
			int j = 0;

			// println ===���� ���� ����Ʈ===
			System.out.println("===== " + enemyTeamName + " ���� List =====");
			while (rs.next()) {
				String name = rs.getString("PLAYERS_NAME");
				int stat = rs.getInt("PLAYERS_STAT");
				enemyList.add(new PlayerVO(name, stat));
				System.out.println(enemyList.get(j).toString());
				j++;

			}
			System.out.println();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return result;

	}

	// �̰� �Ʒ��� �ִ� game method���� Ȱ���ϰ� ������
	// �׷� ��ü�� �ʿ��ϴϱ�...?

	public int playerPick(String newid) {
		getConn();
		// database����
		int playerPick = 0;

		ArrayList<Integer> playerList = new ArrayList<Integer>();

		String id = newid;
		String sql = "select players_no from players where user_id = ?";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			// resultQuery ���� id�� ���� ������ ���ž�

			while (rs.next()) {
				// rs�� �����ϴ� ����
				int playerNo = rs.getInt("players_no");
				// playerNo�� �޾ƿͼ�
				playerList.add(playerNo);
				// playerList�� �־��ֱ�
			}

			// playerList���� �������� �� �� �̾Ƽ� �����ֱ�
			// ���⼭�� ���ڸ� ��ȯ���ִ� �뵵
			playerPick = playerList.get(ran.nextInt(playerList.size()));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return playerPick;
		// id�ٸ��� �Է��ϸ� �׿� �´� playerPick
	}

	// playerNo �Է¹����� stat ��ȯ���ִ� �޼ҵ�
	public int playerStat(int playerNo) {
		//
		int stat = 0;

		getConn();
		String getPlayerStat = "SELECT players_stat FROM players WHERE players_no = ?";
		try {
			psmt = conn.prepareStatement(getPlayerStat);
			psmt.setInt(1, playerNo);
			rs = psmt.executeQuery();

			while (rs.next()) {
				stat = rs.getInt("players_stat");
			}
			// System.out.println("stat >> " + stat);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stat;
	}

	public String getPlayerName(int PlayerNo) {
		getConn();
		String name = null;

		try {
			String getPlayerName = "SELECT players_name FROM players WHERE players_no = ?";
			psmt = conn.prepareStatement(getPlayerName);
			psmt.setInt(1, PlayerNo);
			rs = psmt.executeQuery();
			while (rs.next()) {
				name = rs.getString("players_name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return name;
	}

	public String getTeamName(String id) {

		String teamName = "";

		try {
			String sql = "select user_team from users where user_id = ?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			rs.next();
			teamName = rs.getString("user_team");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return teamName;

	}

	// inning id �޾ƿ��� stat, player name, team ��� ���� �� �� ���ݾ�

	public int strike() {
		System.out.println("STRIKE");
		return 0;
	}

	public int hit() {
		System.out.println("HIT!! 1�� ȹ��");
		return 1;
	}

	public int homerun() {
		System.out.println("HOMERUN!! 2�� ȹ��");
		return 2;
	}

	public int inning(int userPlayer, int enemyPlayer) {

		// player Name �������
		String userPlayerName = getPlayerName(userPlayer);
		String enemyPlayerName = getPlayerName(enemyPlayer);

		// stat �������
		int userPlayerStat = playerStat(userPlayer);
		int enemyPlayerStat = playerStat(enemyPlayer);

		System.out.print("�츮�� ���� : " + userPlayerName);
		System.out.print(" / ���� : " + userPlayerStat);
		System.out.print("  VS ");
		System.out.print("����� ���� : " + enemyPlayerName);
		System.out.print(" / ���� : " + enemyPlayerStat);
		System.out.println();

		// enemyPlayer�� stat�������
		int match = userPlayerStat - enemyPlayerStat;
		if (match <= 10) {
			int lotto = ran.nextInt(9) + 1;
			if (lotto == 1) {
				System.out.println("!!!!!Ȩ��!!!!! ");
				return homerun();
			} else if (lotto <= 3) {
				return hit();
			} else {
				return strike();
			}

		} else if (match <= 50) {
			int lotto = ran.nextInt(9) + 1;
			if (lotto <= 2) {
				System.out.println("!!!!!Ȩ��!!!!! ");
				return homerun();
			} else if (lotto <= 4) {
				return strike();
			} else {
				return hit();
			}
		} else {
			int lotto = ran.nextInt(9) + 1;
			if (lotto == 1) {
				System.out.println("��Ʈ����ũ..? ������ �����ϼ���");
				return strike();
			} else if (lotto <= 3) {
				return hit();
			} else {
				return homerun();
			}
		}

	}

	public int getScore(String userId) {
		getConn();
		int score = 0;
		try {
			String userScoreSql = "SELECT user_score FROM users WHERE user_id = ?";
			psmt = conn.prepareStatement(userScoreSql);
			psmt.setString(1, userId);
			rs = psmt.executeQuery();
			while (rs.next()) {
				score = rs.getInt("user_score");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return score;
	}

	public void updateScore(String userId, int userScore) {
		getConn();
		int cnt;

		String updateUserScore = "UPDATE users SET user_score = ? WHERE user_id = ?";
		try {
			psmt = conn.prepareStatement(updateUserScore);
			psmt.setInt(1, userScore);
			psmt.setString(2, userId);
			cnt = psmt.executeUpdate();
			if (cnt > 0) {
				System.out.println("���ھ� ������Ʈ �Ϸ�");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// game method
	// �ϴ��� String id
	public void game(String newId, String enemyId) throws ClassNotFoundException {
// thorws�κ� �����ϱ�
		getConn();

		String id = newId;
		String enemy = enemyId;

		int userStat = 0;
		int enemyStat = 0;

		int strike = 0;
		int hit = 0;
		int homerun = 0;

		int userScore = getScore(id); // database�� ����� user�� ����
		int userGameScore = 0;// ���ӿ��� ��� ����

		// ���� ���� �� �� sql Ȱ���ؼ� score update

		// System.out.println("���� ����� ���ھ� �޾ƿ��� >> " + getScore(id));

		playerPick(id);
		// System.out.println("����� ���� ���� �޾ƿ��� >> " + playerPick(id));
		// System.out.println("����� ���� ���� �̸� >> " + getPlayerName(playerPick(id)));
		playerPick(enemy);
		// System.out.println("���� ���� ���� �޾ƿ��� >> " + playerPick(enemy));
		// System.out.println("���� ���� ���� �̸� >> " + getPlayerName(playerPick(enemy)));

		while (true) {
			System.out.print("[1] �÷���! [2] ����ҷ��� ��");
			int goOrStop = sc.nextInt();
			// ====== ���� ���� ======
			if (goOrStop == 1) {

				int gameCnt = 0;
				int choice = 0;
				System.out.println("���� ���� ! ");

				while (true) {
					gameCnt++;
					System.out.println("===" + gameCnt + "�̴� ���� === ");
					
					int result = inning(playerPick(id), playerPick(enemy));
					if (result == 0) {
						strike++;
						if (strike == 10) {// strike ==3���� ��ġ��
							System.out.println("---- �� �� �� �� �� �� ----");
							break;
						}
					}
					userGameScore += result;

					// ���� ���� ��� �Ұ��� �����
					if (gameCnt == 9) {
						break;
					} else {
						System.out.print("[1] ���� �̴� [2] ������� >> ");
						choice = sc.nextInt();
					}
					if (choice == 2) {
						break;
					}
				}

				// scoreUpdate method

				System.out.println("������ ����ƽ��ϴ�.");

				if (strike == 10) {
					System.out.println("��� ��� : �й� ");

				} else if (gameCnt == 9) {
					if (choice != 2) {
						System.out.println("��� ��� : �¸�");
						System.out.println("���ϵ帳�ϴ�!! �¸��� ���� �������� ���� 1�� �߰�����ϼ���!");
						playerInput(id);
						showPlayerList(id);
					}
				}

				// score update�����ϱ� ��ȭ�ؾ���
				userScore += userGameScore;
				updateScore(id, userScore);
				System.out.println("���� ����� �� ������ >> " + getScore(id));
				break;
			} else if (goOrStop == 2) {
				System.out.println("������...!!");
				break;
			} else {
				System.out.println("[1]�� [2]�߿��� ������!");
			}
		}
		// gameCnt

//		ArrayList<Integer>userPlayerList = new ArrayList<Integer>();

		// try {

		// int userPick = userPlayerList.get(ran.nextInt(userPlayerList.size()));
		// System.out.println("userPick >> " + userPick);
		// userPick���� user���� ���� ��ȣ�� �������

		// �츮�� player stat

//			String sql2 = "SELECT * FROM players WHERE players_no = ?";
//			psmt = conn.prepareStatement(sql2);
//			psmt.setInt(1, userPick);
//			rs = psmt.executeQuery();
//			while(rs.next()) {
//			userStat = rs.getInt("players_stat");
//			System.out.println("userPlayerNo >> " + userPick);
//			System.out.println("userStat >> " + userStat);
//			}

		// ==== ����� ���� ����� ���� �κ� ====
		// enemyId�� �޾ƿ� -> �̰� Ȱ���ؼ� ����� �������� �� �� �̾��ֱ�
		// ��, ������ 1ȸ�� ����� ������ ������ �ٲ��� ��

//			enemyId = enemyPick(id);
		// enemyId
//			int enemyNum = playerPick(enemyId);//������ ���� ��ȣ�� �ϳ��� �������� ���

//			ArrayList<Integer>enemyPlayerList = new ArrayList<Integer>();
//			
//			String sql3 = "SELECT players_no FROM players WHERE user_id = ?";
//			psmt = conn.prepareStatement(sql3);
//			psmt.setString(1, enemy);
//			rs = psmt.executeQuery();
//			while(rs.next()) {
//				int enemyPlayer = rs.getInt("players_no");
//				enemyPlayerList.add(enemyPlayer);
//			}

//			while(gameCnt < 10) { // game 9ȸ ����ɶ����� ����
//				// ������ ����� ������ ���� ���� �޶������ϴϱ� ���⼭ ���� ���� �̾��ֱ�
//				System.out.println("***"+gameCnt+"ȸ ���� ***");
//				// int enemyPick = enemyPlayerList.get(ran.nextInt(enemyPlayerList.size()));
//				
//				
//				// enemyPick�� stat �������ֱ�
//				String sql4 = "SELECT * FROM players WHERE players_no = ?";
//				psmt = conn.prepareStatement(sql4);
//				psmt.setInt(1, enemyPick);
//				rs = psmt.executeQuery();
//				while(rs.next()) {
//				enemyStat = rs.getInt("players_stat");
//				System.out.println("enemyPlayerNo >> " + enemyPick);
//				System.out.println("enemyStat >> " + enemyStat);
//				}
//				
//				
//				int match = userStat - enemyStat;
//				System.out.println("match : "+match);
//				if(match <= 10) {
//					if(strike == 3) {
//						System.out.println("!!�� �� �� ��!!");
//						break;
//					}else if(strike==1) {
//						System.out.println("strike : �ܡۡ�");
//					}else if(strike==2) {
//						System.out.println("strike : �ܡܡ�");
//					}
//					strike++;
//					System.out.println("STRIKE");
//				}else if(match <= 50) {
//					hit++;
//					userGameScore++;
//					System.out.println("HIT !! 1 �� ȹ��");
//					System.out.println("���� ���� >> " + userGameScore);
//				}else{
//					homerun++;
//					userGameScore += 2;
//					System.out.println("HOMERUN !!2 �� ȹ��");
//					System.out.println("���� ���� >> " + userGameScore);
//					
//				}
//				System.out.println("=== "+gameCnt + "ȸ ����! ===");
//				gameCnt++;
//				
//				
//			}

//			if(gameCnt==9) {
//				if(strike != 3) {
//					System.out.println("���ӿ��� �¸��ϼ̽��ϴ�.");
//					System.out.println("���ο� ���� 1���� ������ּ���.");
//					
//					int cnt = 0;
//					System.out.println("ID >> ");					
//					String newid = sc.next();
//					System.out.println("PW >> ");					
//					String newPw = sc.next();
//					System.out.println("TEAM >> ");					
//					String newTeam = sc.next();
//
//					// �ٵ� team�� ���� team���� �̸� ������ָ� ���� ������?
//					// �ϴ��� �׳� �Է¹���
//					// �����Ǹ� �� �κ� �����ϱ� -> ���� team�־��ֵ���
//
//					// ���ο� ������� method�� ���� ����
//					// return �ϰ� �ؼ� �ұ�?
//					
//					int newStat = ran.nextInt(100)+1;
//					
//					String updatePlayer = "insert into users values(?,?,?,?)";
//					
//					psmt = conn.prepareStatement(updatePlayer);
//					
//					psmt.setString(1, newid);
//					psmt.setString(2, newPw);
//					psmt.setString(3, newTeam);
//					psmt.setInt(4, newStat);
//					cnt = psmt.executeUpdate();
//				}
//				
//				
//			}else {
//				System.out.println("�й��ϼ̽��ϴ�.");				
//			}

//			System.out.println("-- ����� -- ");
//			System.out.println("HIT : "+ hit +"��");
//			System.out.println("HOMERUN : "+ homerun +"��");
//			System.out.println("ȹ������ : "+ userGameScore);
//			
//			System.out.println("������ ����ƽ��ϴ�.");
//			
//			
//			
//			String updateUserScore = "UPDATE users SET user_score = ? WHERE user_id = ?";
//			psmt = conn.prepareStatement(updateUserScore);
//			psmt.setInt(1, userGameScore+userScore);
//			psmt.setString(2, id);
//			int row = psmt.executeUpdate();
//			// �̰� catch �ȳ־��൵ �ǳ�?
//			// ��� �ȵ�....
//			if(row > 0) {
//				System.out.println("���� ������Ʈ �Ϸ�");
//			}else {
//				System.out.println("�����߻�");
//			}
//			
//			String checkUserScore = "SELECT user_score FROM users WHERE user_id = ?";
//			psmt = conn.prepareStatement(checkUserScore);
//			psmt.setString(1, id);
//			rs = psmt.executeQuery();
//			
//			while(rs.next()) {
//				int checkScore = rs.getInt("user_score");
//				System.out.println("���� ��ü ������ >> " + checkScore);
//			}
//			
//			
//			
//			
//			
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}   

	}

}
