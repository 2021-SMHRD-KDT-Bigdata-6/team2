import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
//커밋
		Scanner sc = new Scanner(System.in);
		userDAO dao = new userDAO();

		while (true) {

			System.out.print("[1]로그인  [2]회원가입  [3]종료");
			int select = sc.nextInt();

			if (select == 1) {
				// login
				System.out.println("--로그인--");
				System.out.print("ID입력 : ");
				String id = sc.next();
				System.out.print("PW입력 : ");
				String pw = sc.next();
				
				userVO vo = new userVO(id, pw);
				userVO info = dao.login(vo);
				
				if(info != null) {
					System.out.println("로그인 성공!");
					System.out.println(info.getId() + "님 환영합니다!");
				}else {
					System.out.println("로그인 실패..");
				}
				
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
						System.out.println("***BaseBall 랭킹!***");
						ArrayList<userVO> ranList = dao.ranking();
						for (int i = 0; i < ranList.size(); i++) {
							System.out.println(ranList.get(i));
						}
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
				System.out.print("아이디를 입력하세요 >> ");
				String id = sc.next();
				System.out.print("비밀번호를 입력하세요 >> ");
				String pw = sc.next();
				System.out.print("구단명을 입력하세요 >> ");
				String team = sc.next();
				
				
				userVO vo = new userVO(id, pw, team);
				int cnt = dao.signup(vo);
				
				if(cnt > 0) {
					System.out.println("회원가입 성공!");
				}else {
					System.out.println("회원가입 실패...");
				}
				
				
				
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
