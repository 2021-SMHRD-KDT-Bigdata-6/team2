import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		while (true) {

			System.out.print("[1]로그인  [2]회원가입  [3]종료");
			int select = sc.nextInt();

			if (select == 1) {
				// login
				while (true) {
					System.out.print("[1]경기시작  [2]랭킹보기  [3]이전화면");
					int select2 = sc.nextInt();
					if (select2 == 1) {
						// game
						// randomPick()
						// show_playerList
						
						// playerInput(선수등록)
						
						// ranking
					} else if (select2 == 2) {
						// ranking
					} else if (select2 == 3) {
						System.out.println("메인으로 돌아갑니다.");
						break;
					} else {
						System.out.println("1~3번 중에 입력해주세요!!");
					}
				}

			} else if (select == 2) {
				// signup
				// playerInput(선수등록)
				// show_playerList
			} else if (select == 3) {
				System.out.print("진짜 종료하시겠습니까?ㅜ >> (y/n)");
				String answer = sc.next();
				if (answer.equals("y")) {
					break;
				}
			} else {
				System.out.println("1~3번 중에 입력해주세요!!");
			}

		}

	}

}
