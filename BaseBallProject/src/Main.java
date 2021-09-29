import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
//Ŀ��
		Scanner sc = new Scanner(System.in);
		userDAO dao = new userDAO();
		PlayerDAO daoP = new PlayerDAO();

		while (true) {

			System.out.print("[1]�α���  [2]ȸ������  [3]����");
			int select = sc.nextInt();

			if (select == 1) {
				// login
				System.out.println("--�α���--");
				System.out.print("ID�Է� : ");
				String id = sc.next();
				System.out.print("PW�Է� : ");
				String pw = sc.next();
				
				userVO vo = new userVO(id, pw);
				userVO info = dao.login(vo);
				
				if(info != null) {
					System.out.println("�α��� ����!");
					System.out.println(info.getId() + "�� ȯ���մϴ�!");
				}else {
					System.out.println("�α��� ����..");
				}
				
				while (true) {
					System.out.print("[1]������  [2]��ŷ����  [3]����ȭ��");
					int select2 = sc.nextInt();
					if (select2 == 1) {
						// game
						// randomPick()
						// show_playerList
						
						// playerInput(�������)
						
						// ranking
					} else if (select2 == 2) {
						// ranking
						System.out.println("***BaseBall ��ŷ!***");
						ArrayList<userVO> ranList = dao.ranking();
						for (int i = 0; i < ranList.size(); i++) {
							System.out.println(ranList.get(i));
						}
					} else if (select2 == 3) {
						System.out.println("�������� ���ư��ϴ�.");
						break;
					} else {
						System.out.println("1~3�� �߿� �Է����ּ���!!");
					}
				}

			} else if (select == 2) {
				// signup
				// playerInput(�������)
				// show_playerList
				System.out.print("���̵� �Է��ϼ��� >> ");
				String id = sc.next();
				System.out.print("��й�ȣ�� �Է��ϼ��� >> ");
				String pw = sc.next();
				System.out.print("���ܸ��� �Է��ϼ��� >> ");
				String team = sc.next();
				
				
				userVO vo = new userVO(id, pw, team);
				int cnt = dao.signup(vo);
		
				if(cnt > 0) {
					System.out.println("ȸ������ ����!");
				}else {
					System.out.println("ȸ������ ����...");
				}
				
				System.out.println("====���������� �����մϴ�.====");
				System.out.println("���ʿ� 5���� ������ ������ �� �ֽ��ϴ�.");
				
				int cnt2 = 0;
				while (cnt2 < 5) {
					daoP.playerInput(id);
					cnt2++;
				}
				System.out.println();
				daoP.showPlayerList(id);
				// show_playerList
				
			} else if (select == 3) {
				System.out.print("��¥ �����Ͻðڽ��ϱ�?�� >> (y/n)");
				String answer = sc.next();
				if (answer.equals("y")) {
					System.out.println("������ �����մϴ�!");
					break;
				}
			} else {
				System.out.println("1~3�� �߿� �Է����ּ���!!");
			}

		}

	}

}
