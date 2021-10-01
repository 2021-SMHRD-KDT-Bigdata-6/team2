import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javazoom.jl.player.MP3Player;

public class PlayerDAO {

	Scanner sc = new Scanner(System.in);
	Random ran = new Random();

	private Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;
	
	
	
	MP3Player mp3 = new MP3Player();

	MusicPlayer player = new MusicPlayer();

	Music music;

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

	public int getPlayerNumber(String id) {
		getConn();
		int cntPlayerNumber = 0;

		try {
			String sql = "select * from players where user_id = ?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			while (rs.next()) {
				cntPlayerNumber++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cntPlayerNumber;
	}

	public void showPlayerList(String newId) {
		getConn();
		ArrayList<PlayerVO> playerList = new ArrayList<PlayerVO>();
		String id = newId;

		try {

			String teamName = getTeamName(id);

			// System.out.println("축하드립니다!! " + teamName + "이(가) 결성됐습니다!!");
			System.out.println();
			System.out.println("===== " + teamName + " 선수 List =====");
			// 팀명이 나오게

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

	// void 바꿔서 enemyPick -> id return하도록
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
				// rs가 존재한다면
				String otherUserId = rs.getString("user_id");
				// userid를 rs의 user_id column에서 받아와서
				userList.add(otherUserId);
				// userList에 userid 담아주기
			}

			String enemyId = userList.get(ran.nextInt(userList.size()));
			result = enemyId;
			// 여기서 오류 발생 -> 해결
			// id 해결, userList 채워줌
			// enemyId 는, userList에서 userList길이만큼 랜덤숫자 중 하나 걸린거

			ArrayList<PlayerVO> myList = new ArrayList<PlayerVO>();
			ArrayList<PlayerVO> enemyList = new ArrayList<PlayerVO>();
			// 상대방list

			String userTeamName = getTeamName(userId);
			String enemyTeamName = getTeamName(enemyId);

			System.out.println();
			System.out.println("***** 오늘의 매치 *****");
			System.out.println(userTeamName + "  VS  " + enemyTeamName);
			System.out.println();

			String sql1 = "select * from players where user_id = ? order by players_no";
			psmt = conn.prepareStatement(sql1);
			psmt.setString(1, userId);
			rs = psmt.executeQuery();

			int k = 0;

			System.out.println("===== " + userTeamName + " 선수 List =====");
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

			// println ===상대방 선수 리스트===
			System.out.println("===== " + enemyTeamName + " 선수 List =====");
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

			while (rs.next()) {
				int playerNo = rs.getInt("players_no");
				playerList.add(playerNo);
			}

			playerPick = playerList.get(ran.nextInt(playerList.size()));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return playerPick;
		// id다르게 입력하면 그에 맞는 playerPick
	}

	// playerNo 입력받으면 stat 반환해주는 메소드
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

	public String getUserId(int playerNo) {
		getConn();
		String userId = null;
		try {
			String getUserId = "SELECT user_id FROM players WHERE players_no = ?";
			psmt = conn.prepareStatement(getUserId);
			psmt.setInt(1, playerNo);
			rs = psmt.executeQuery();
			rs.next();
			userId = rs.getString("user_id");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userId;
	}
	
	public void baseballAscii() {
		System.out.println(
				"                                              _-- _      ***  \r\n"
				+"                                           __(_  )     **\r\n"
				+"                                             <  (D)  .**\r\n"
				+"                                            .~ \\ /~```\r\n"
				+"                                          .~    V    *_ \r\n "
				+"                                         (   (___. {:)-./\r\n"
				+"                                           ~._____.(:}\r\n"
				+"                     _____                  |       | \r\n"
				+"                   .'     '.               /        \\ \r\n"
				+"                  /  ball   |            /          \\ \r\n"
				+"                 |          |            /   /  \\    \\ \r\n"
				+"                  \\.-''''-./           /   /    \\   \\ \r\n"
				+"                   '.____.'             \\__/      \\__/ \r\n"
				+"                                         / /        | | \r\n"
				+"                                        .^V^.      .^V^. \r\n"
				+"                                        +-+        +-+ \r\n");
		
	}
	
	public void hitAscii() {
		System.out.println(
				"                   _         ____                                                   __         \r\n"
				+ "                / \\     .'    '.        ____......------\"\"\"\"\"\"\"\"`````  `     \\       \r\n"
				+ "               |   `::-/'-....-'\\--\"\"```                                            |         \r\n"
				+ "               |    :: |         ||                                                    |        \r\n"
				+ "               |   ,:'-\\.-''''-.//--..___                                             /          \r\n"
				+ "                \\_/     '.____.'        ````````````\"\"\"\"\"\"\"-----------------'");
	}

	public void lottoHitAscii() {
		System.out.println("            ┏━┳━┳━┓     ┏━┳━┳━┓     ┏━┳━┳━┓");
		System.out.println("            ┃H┃I┃T┃     ┃H┃I┃T┃     ┃H┃I┃T┃");
		System.out.println("            ┗━┻━┻━┛     ┗━┻━┻━┛     ┗━┻━┻━┛");

	}
	
	public void homerunAscii() {
				System.out.println("              ＿人人人人人人人＿");
				System.out.println("              ＞   홈  런  ＜");
				System.out.println("              ＞   홈  런  ＜");
				System.out.println("              ＞   홈  런  ＜");
				System.out.println("              ￣Y^Y^Y^Y^Y^￣");

	}
	
	
	public void lottoHomeRun() {

		System.out.println(
				  "##     ##  #######  ##     ## ######## ########  ##     ## ##    ## \r\n"
				+ "##     ## ##     ## ###   ### ##       ##     ## ##     ## ###   ## \r\n"
				+ "##     ## ##     ## #### #### ##       ##     ## ##     ## ####  ## \r\n"
				+ "######### ##     ## ## ### ## ######   ########  ##     ## ## ## ## \r\n"
				+ "##     ## ##     ## ##     ## ##       ##   ##   ##     ## ##  #### \r\n"
				+ "##     ## ##     ## ##     ## ##       ##    ##  ##     ## ##   ### \r\n"
				+ "##     ##  #######  ##     ## ######## ##     ##  #######  ##    ## ");
		
	}
	public int strike() {
		System.out.println();
		System.out.println("       ※※※※※ 투수공을 바라보고만 있는 타자!!! ※※※※※");
		System.out.println("               >> STRIKE ");
		baseballAscii();

		player.stop();
		player.play(3);
		return 0;
	}

	public int lottoStrike() {
		System.out.println();
		System.out.println("         ※※※※※ 스트라이크라니..? ※※※※※");
		System.out.println("               \\ STRIKE //");
		baseballAscii();
		player.stop();
		player.play(3);

		return 0;
	}

	public int lottoHit() {
		System.out.println();
		System.out.println("            ※※※※※세이프티 번트 성공 ※※※※※");
		lottoHitAscii();
		hitAscii();
		System.out.println();
		player.stop();
		player.play(1);
		return 1;
	}

	public int hit() {
		System.out.println();
		System.out.println(" ※※※※※ 우리팀의 간판 타자! ※※※※※");
		hitAscii();
		System.out.println(" >>> HIT : 1점 획득");
		System.out.println();
		player.stop();
		player.play(1);
		return 1;
	}

	public int homerun() {
		System.out.println();
		System.out.println("         ※※※※※ 우리팀 거포 ※※※※※");
		homerunAscii();
		System.out.println(" >>> HOMERUN : 2점 획득");
		System.out.println();
		player.stop();
		player.play(2);
		return 2;
	}

	public int lottoHomerun() {
		System.out.println();
		System.out.println("      ※※※※※ 나도 홈런칠 수 있어~! ※※※※※");
		System.out.println();
		lottoHomeRun();
		System.out.println();

		player.stop();
		player.play(2);

		return 2;
	}

	public String getTeamId(int playerNo) {
		return null;
	}

	public int inning(int userPlayer, int enemyPlayer) {

		// player Name 갖고오기
		String userPlayerName = getPlayerName(userPlayer);
		String enemyPlayerName = getPlayerName(enemyPlayer);
		// getTeamName => String id(team id 필요)

		// stat 갖고오기
		int userPlayerStat = playerStat(userPlayer);
		int enemyPlayerStat = playerStat(enemyPlayer);

		System.out.print(getTeamName(getUserId(userPlayer)) + "팀 선수 : " + userPlayerName);
		System.out.print("(" + userPlayerStat + ")");
		System.out.print("  VS ");
		System.out.print(getTeamName(getUserId(enemyPlayer)) + "팀 선수 : " + enemyPlayerName);
		System.out.print("(" + enemyPlayerStat + ")");
		System.out.println();

		int match = userPlayerStat - enemyPlayerStat;

		if (match == 0) {
			System.out.println("     ＿人人人人人人＿");
			System.out.println("     ＞벤치 클리어링＜");
			System.out.println("     ＞(같은능력치)＜");
			System.out.println("     ￣Y^Y^Y^Y^Y￣");
			return -1;
		} else if (match <= 10) {
			int lotto = ran.nextInt(9) + 1;
			if (lotto == 1) {
				return lottoHomerun();
			} else if (lotto <= 3) {
				return lottoHit();
			} else {
				return strike();
			}

		} else if (match <= 50) {
			int lotto = ran.nextInt(9) + 1;
			if (lotto <= 2) {
				return lottoHomerun();
			} else if (lotto <= 4) {
				return lottoStrike();
			} else {
				return hit();
			}
		} else {
			int lotto = ran.nextInt(9) + 1;
			if (lotto == 1) {
				return lottoStrike();
			} else if (lotto <= 3) {
				return lottoHit();
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

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

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

		int userScore = getScore(id);
		int userGameScore = 0;

		playerPick(id);
		playerPick(enemy);

		while (true) {
			System.out.print("[1] 플레이! [2] 기권할래요 ㅜ >> ");
			
			int goOrStop = sc.nextInt();
			// ====== 게임 진행 ======
			if (goOrStop == 1) {

				int gameCnt = 0;
				int choice = 0;
				System.out.println("      /            /");
				System.out.println("     / GAME START / ");
				System.out.println("    /            /");

				while (true) {
					gameCnt++;
					System.out.println("≫ ──── ≪ " + gameCnt + "이닝 ≫ ──── ≪");

					int result = inning(playerPick(id), playerPick(enemy));
					userGameScore += result;

					if (result == -1) {
						
						System.out.println("싸움이 나서 경기 중단");
					} else if (result == 0) {
						// STRIKE일 경우
						strike++;
						if (strike == 3) {
							System.out.println();
							System.out.println("           ※※※※※ 루킹스트라이크 아웃 ※※※※※");
							System.out.println(
									 "                 ┳┳    ┳┳\r\n"
									+ "                 삼     삼   \r\n"
									+ "               진      진　  　\r\n"
									+ "                 아      아   \r\n"
									+ "               웃  ━━━━━ 웃");
							
							System.out.println();
							System.out.println("총 획득 점수 : " + userGameScore);
							player.stop();
							break;
						}else if(strike == 1) {
							System.out.println("S : ●○○");
							System.out.println();
						}else if(strike == 2) {
							System.out.println("S : ●●○");
							System.out.println();
						}
					}

					if (gameCnt == 9) {
						System.out.println("총 획득 점수 : " + userGameScore);
						player.stop();
						break;
					} else {
						System.out.print("[1] 다음 이닝 [2] 경기포기 >> ");
						choice = sc.nextInt();
					}
					if (choice == 2) {
						System.out.println("획득한 점수가 모두 사라졌습니다!!");
						player.stop();
						userGameScore = 0;
						break;
					}
				}

				// scoreUpdate method

				System.out.println("게임이 종료됐습니다.");

				if (strike == 3) {

					System.out.println("≫ ──── 게임결과 : 패배 ──── ≪");

				} else if (gameCnt == 9) { if (choice != 2) {
	                  System.out.println();
	                  System.out.println("≫ ──── 게임결과 : 승리 ──── ≪");
	                  int numberPlayers = getPlayerNumber(id);

	                  if (numberPlayers < 9) {
	                     System.out.println();
	                     System.out.println("┌────────────────────┐");
	                     System.out.println("      축하드립니다!   ");
	                     System.out.println("   승리에 대한 보상으로  ");
	                     System.out.println(" 선수 1명 추가 등록하세요!");
	                     System.out.println("└────────────────────┘");
	                     
	                     playerInput(id);
	                     showPlayerList(id);
	                  } else {
	                     System.out.println();
	                     System.out.println("한 팀당 최대 인원수 초과로 추가 선수등록 보상을 받을 수 없습니다.");
	                  }
	               }
	            }

				userScore += userGameScore;
				updateScore(id, userScore);
				System.out.println("현재 당신의 총 점수는 >> " + getScore(id));
				break;
			} else if (goOrStop == 2) {
				System.out.println("겁쟁이...!!");
				break;
			} else {
				System.out.println("[1]과 [2]중에서 고르세요!");
			}
		}
		// gameCnt

//		ArrayList<Integer>userPlayerList = new ArrayList<Integer>();

		// try {

		// int userPick = userPlayerList.get(ran.nextInt(userPlayerList.size()));
		// System.out.println("userPick >> " + userPick);
		// userPick에는 user팀의 선수 번호가 담겨있음

		// 우리팀 player stat

//			String sql2 = "SELECT * FROM players WHERE players_no = ?";
//			psmt = conn.prepareStatement(sql2);
//			psmt.setInt(1, userPick);
//			rs = psmt.executeQuery();
//			while(rs.next()) {
//			userStat = rs.getInt("players_stat");
//			System.out.println("userPlayerNo >> " + userPick);
//			System.out.println("userStat >> " + userStat);
//			}

		// ==== 여기는 이제 상대팀 고르는 부분 ====
		// enemyId는 받아옴 -> 이걸 활용해서 상대팀 랜덤으로 한 명 뽑아주기
		// 단, 게임이 1회씩 진행될 때마다 선수는 바뀌어야 함

//			enemyId = enemyPick(id);
		// enemyId
//			int enemyNum = playerPick(enemyId);//상대방팀 선수 번호중 하나를 랜덤으로 얻기

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

//			while(gameCnt < 10) { // game 9회 진행될때까지 진행
//				// 게임이 진행될 때마다 상대방 선수 달라져야하니까 여기서 랜덤 선수 뽑아주기
//				System.out.println("***"+gameCnt+"회 시작 ***");
//				// int enemyPick = enemyPlayerList.get(ran.nextInt(enemyPlayerList.size()));
//				
//				
//				// enemyPick의 stat 저장해주기
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
//						System.out.println("!!삼 진 아 웃!!");
//						break;
//					}else if(strike==1) {
//						System.out.println("strike : ●○○");
//					}else if(strike==2) {
//						System.out.println("strike : ●●○");
//					}
//					strike++;
//					System.out.println("STRIKE");
//				}else if(match <= 50) {
//					hit++;
//					userGameScore++;
//					System.out.println("HIT !! 1 점 획득");
//					System.out.println("현재 점수 >> " + userGameScore);
//				}else{
//					homerun++;
//					userGameScore += 2;
//					System.out.println("HOMERUN !!2 점 획득");
//					System.out.println("현재 점수 >> " + userGameScore);
//					
//				}
//				System.out.println("=== "+gameCnt + "회 종료! ===");
//				gameCnt++;
//				
//				
//			}

//			if(gameCnt==9) {
//				if(strike != 3) {
//					System.out.println("게임에서 승리하셨습니다.");
//					System.out.println("새로운 선수 1명을 등록해주세요.");
//					
//					int cnt = 0;
//					System.out.println("ID >> ");					
//					String newid = sc.next();
//					System.out.println("PW >> ");					
//					String newPw = sc.next();
//					System.out.println("TEAM >> ");					
//					String newTeam = sc.next();
//
//					// 근데 team은 기존 team으로 미리 등록해주면 좋지 않을까?
//					// 일단은 그냥 입력받자
//					// 여유되면 이 부분 수정하기 -> 기존 team넣어주도록
//
//					// 새로운 선수등록 method로 따로 빼기
//					// return 하게 해서 할까?
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
//				System.out.println("패배하셨습니다.");				
//			}

//			System.out.println("-- 경기결과 -- ");
//			System.out.println("HIT : "+ hit +"개");
//			System.out.println("HOMERUN : "+ homerun +"개");
//			System.out.println("획득점수 : "+ userGameScore);
//			
//			System.out.println("게임이 종료됐습니다.");
//			
//			
//			
//			String updateUserScore = "UPDATE users SET user_score = ? WHERE user_id = ?";
//			psmt = conn.prepareStatement(updateUserScore);
//			psmt.setInt(1, userGameScore+userScore);
//			psmt.setString(2, id);
//			int row = psmt.executeUpdate();
//			// 이거 catch 안넣어줘도 되나?
//			// 계속 안들어감....
//			if(row > 0) {
//				System.out.println("점수 업데이트 완료");
//			}else {
//				System.out.println("오류발생");
//			}
//			
//			String checkUserScore = "SELECT user_score FROM users WHERE user_id = ?";
//			psmt = conn.prepareStatement(checkUserScore);
//			psmt.setString(1, id);
//			rs = psmt.executeQuery();
//			
//			while(rs.next()) {
//				int checkScore = rs.getInt("user_score");
//				System.out.println("현재 전체 점수는 >> " + checkScore);
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
