import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	public String login() {
		userVO info = null;
		String id = null;
		
		int login = 0;
		while (login == 0) {
			getConn();
			System.out.println("--로그인--");
			System.out.print("ID입력 : ");
			id = sc.next();
			System.out.print("PW입력 : ");
			String pw = sc.next();

			userVO vo = new userVO(id, pw);

			try {
				String sql = "select * from users where user_id = ? and user_pw = ?";
				psmt = conn.prepareStatement(sql);
				psmt.setString(1, vo.getId());
				psmt.setString(2, vo.getPw());
				rs = psmt.executeQuery();
				if (rs.next()) {
					id = rs.getString("user_id");
					pw = rs.getString("user_pw");
					String team = rs.getString("user_team");
					int score = rs.getInt("user_score");

					info = new userVO(id, pw, team, score);
				}

				if (info != null) {
					System.out.println("로그인 성공!");
					System.out.println(id + "님 환영합니다!");
					login++;
				} else {
					// 아이디가 틀렸을떄, 비밀번호가 틀렸을때 판별하기
					if (check(vo.getId()) == true) {
						System.out.println("비밀번호가 틀렸습니다. 다시 입력해주세요.");
					} else if(check(vo.getId()) != true) {
						System.out.println("존재하지 않는 아이디입니다. 다시 입력해주세요.");
					}
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}

		}

		return id;
	}

	// 회원가입 아이디중복
	public boolean check(String id) {
		getConn();
		String sql = "select user_id from users";
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();

			while (rs.next()) {
				if (rs.getString("user_id").equals(id)) {
					return true;
				} // ---> 아이디가 중복일때
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false; // ---> 아이디가 중복되는게 없을때
	}

	// 회원가입
	public String signUp() {
		userVO vo = null;
		String id = null;
		int cnt = 0;
		
		while (cnt == 0) {
			getConn();
			System.out.println("--회원가입--");
			System.out.print("아이디를 입력하세요 >> ");
			id = sc.next();
			System.out.print("비밀번호를 입력하세요 >> ");
			String pw = sc.next();
			System.out.print("구단명을 입력하세요 >> ");
			String team = sc.next();
			vo = new userVO(id, pw, team);

			if (check(vo.getId()) == true) {
				System.out.println("이미 존재하는 아이디입니다. 다시 입력해주세요.");
				
			} else {
				try {
					String spl = "insert into users values(?,?,?,?)";

					psmt = conn.prepareStatement(spl);
					psmt.setString(1, vo.getId());
					psmt.setString(2, vo.getPw());
					psmt.setString(3, vo.getTeam());
					psmt.setInt(4, 0);
					cnt = psmt.executeUpdate();

					System.out.println("회원가입 성공!");
					System.out.println("====선수영입을 시작합니다.====");
					System.out.println("최초에 5명의 선수를 영입할 수 있습니다.");
					cnt++;

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					close();
				}
			}
		}

		return id;
	}

	// 랭킹확인하기 ranking

	public ArrayList<userVO> getRanking() {

		getConn();

		ArrayList<userVO> ranList = new ArrayList<userVO>();
		String sql = "select user_id, user_team, user_score from users order by user_score desc";
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString("user_id");
				String team = rs.getString("user_team");
				int score = rs.getInt("user_score");
				userVO vo = new userVO(id, team, score);
				ranList.add(vo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return ranList;
	}

	public void showRanking(ArrayList<userVO> ranList, String id) {
		System.out.println("                 ***BaseBall 랭킹!***");

		for (int i = 0; i < ranList.size(); i++) {
			if (id.equals(ranList.get(i).getId())) {
				System.out.println("============================================================");
				System.out.println("                    " + id + " 님의 랭킹");
				System.out.println("------------------------------------------------------------");
				System.out.printf("%6s", i + 1 + "위   ");
				System.out.printf("%-10s \t%-25s \t%-10s", ranList.get(i).getId(), ranList.get(i).getTeam(),
						ranList.get(i).getScore());
				System.out.println();
				System.out.println("============================================================");

			}
		}
		System.out.printf("%5s %-10s \t%-25s \t%-10s", "순위  ", "아이디", "구단명", "점수");
		System.out.println();
		System.out.println("============================================================");
		for (int i = 0; i < ranList.size(); i++) {

			System.out.printf("%6s", i + 1 + "위   ");
			System.out.printf("%-10s \t%-25s \t%-10s", ranList.get(i).getId(), ranList.get(i).getTeam(),
					ranList.get(i).getScore());
			System.out.println();
			System.out.println("------------------------------------------------------------");
		}
	}

	// 선수리스트 보여주기 show_playerList

}
