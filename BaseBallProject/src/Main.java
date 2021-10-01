import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException {
//커밋
		Scanner sc = new Scanner(System.in);
		userDAO dao = new userDAO();
		PlayerDAO daoP = new PlayerDAO();

		while (true) {

			System.out.print("[1]로그인  [2]회원가입  [3]종료 >> ");
			int select = sc.nextInt();

			if (select == 1) {

				// login 구현하기

				// login

				String id = dao.login();
				while (true) {
					System.out.print("[1]경기시작  [2]랭킹보기  [3]이전화면 >> ");
					int select2 = sc.nextInt();
					if (select2 == 1) { // 경기시작
						daoP.game(id, daoP.enemyPick(id));

					} else if (select2 == 2) {
						// ranking

						dao.showRanking(dao.getRanking(), id);

					} else if (select2 == 3) {
						System.out.println("메인으로 돌아갑니다.");
						break;
					} else {
						System.out.println("1~3번 중에 입력해주세요!!");
					}
				}

			}

			else if (select == 2) {
				// signup
				// playerInput(선수등록)
				// show_playerList
				String id = dao.signUp();
				int cnt2 = 0;
				while (cnt2 < 5) {
					daoP.playerInput(id);
					cnt2++;
				}
				System.out.println();
				daoP.showPlayerList(id);
				// else {
//						System.out.println("회원가입 실패...");
//					}

				// show_playerList

			} else if (select == 3) {
				System.out.print("진짜 종료하시겠습니까?ㅜ >> (y/n)");
				String answer = sc.next();
				if (answer.equals("y")) {
					System.out.println("게임을 종료합니다!");
					break;
				}
			} else {
				System.out.println("1~3번 중에 입력해주세요!!");
			}

		}

	}

}
