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

	// �α���
	public String login() {
		userVO info = null;
		String id = null;
		
		int login = 0;
		while (login == 0) {
			getConn();
			System.out.println("--�α���--");
			System.out.print("ID�Է� : ");
			id = sc.next();
			System.out.print("PW�Է� : ");
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
					System.out.println("�α��� ����!");
					System.out.println(id + "�� ȯ���մϴ�!");
					login++;
				} else {
					// ���̵� Ʋ������, ��й�ȣ�� Ʋ������ �Ǻ��ϱ�
					if (check(vo.getId()) == true) {
						System.out.println("��й�ȣ�� Ʋ�Ƚ��ϴ�. �ٽ� �Է����ּ���.");
					} else if(check(vo.getId()) != true) {
						System.out.println("�������� �ʴ� ���̵��Դϴ�. �ٽ� �Է����ּ���.");
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

	// ȸ������ ���̵��ߺ�
	public boolean check(String id) {
		getConn();
		String sql = "select user_id from users";
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();

			while (rs.next()) {
				if (rs.getString("user_id").equals(id)) {
					return true;
				} // ---> ���̵� �ߺ��϶�
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false; // ---> ���̵� �ߺ��Ǵ°� ������
	}

	// ȸ������
	public String signUp() {
		userVO vo = null;
		String id = null;
		int cnt = 0;
		
		while (cnt == 0) {
			getConn();
			System.out.println("--ȸ������--");
			System.out.print("���̵� �Է��ϼ��� >> ");
			id = sc.next();
			System.out.print("��й�ȣ�� �Է��ϼ��� >> ");
			String pw = sc.next();
			System.out.print("���ܸ��� �Է��ϼ��� >> ");
			String team = sc.next();
			vo = new userVO(id, pw, team);

			if (check(vo.getId()) == true) {
				System.out.println("�̹� �����ϴ� ���̵��Դϴ�. �ٽ� �Է����ּ���.");
				
			} else {
				try {
					String spl = "insert into users values(?,?,?,?)";

					psmt = conn.prepareStatement(spl);
					psmt.setString(1, vo.getId());
					psmt.setString(2, vo.getPw());
					psmt.setString(3, vo.getTeam());
					psmt.setInt(4, 0);
					cnt = psmt.executeUpdate();

					System.out.println("ȸ������ ����!");
					System.out.println("====���������� �����մϴ�.====");
					System.out.println("���ʿ� 5���� ������ ������ �� �ֽ��ϴ�.");
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

	// ��ŷȮ���ϱ� ranking

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
		System.out.println("                 ***BaseBall ��ŷ!***");

		for (int i = 0; i < ranList.size(); i++) {
			if (id.equals(ranList.get(i).getId())) {
				System.out.println("============================================================");
				System.out.println("                    " + id + " ���� ��ŷ");
				System.out.println("------------------------------------------------------------");
				System.out.printf("%6s", i + 1 + "��   ");
				System.out.printf("%-10s \t%-25s \t%-10s", ranList.get(i).getId(), ranList.get(i).getTeam(),
						ranList.get(i).getScore());
				System.out.println();
				System.out.println("============================================================");

			}
		}
		System.out.printf("%5s %-10s \t%-25s \t%-10s", "����  ", "���̵�", "���ܸ�", "����");
		System.out.println();
		System.out.println("============================================================");
		for (int i = 0; i < ranList.size(); i++) {

			System.out.printf("%6s", i + 1 + "��   ");
			System.out.printf("%-10s \t%-25s \t%-10s", ranList.get(i).getId(), ranList.get(i).getTeam(),
					ranList.get(i).getScore());
			System.out.println();
			System.out.println("------------------------------------------------------------");
		}
	}

	// ��������Ʈ �����ֱ� show_playerList

}
