import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		PlayerDAO dao = new PlayerDAO();

		while (true) {

			System.out.print("[1]�α���  [2]ȸ������  [3]���� >> ");
			int select = sc.nextInt();

			if (select == 1) {
				// login
				while (true) {
					System.out.print("[1]������  [2]��ŷ����  [3]����ȭ�� >> ");
					int select2 = sc.nextInt();
					if (select2 == 1) {
						// game
						dao.enemyPick();
						// randomPick()
						// show_playerList

						// playerInput(�������)

						// ranking
					} else if (select2 == 2) {
						// ranking
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
				int cnt = 0;
				while (cnt < 5) {
					dao.playerInput();
					cnt++;
				}
				System.out.println();
				dao.showPlayerList();
				// show_playerList
			} else if (select == 3) {
				System.out.print("��¥ �����Ͻðڽ��ϱ�?�� >> (y/n)");
				String answer = sc.next();
				if (answer.equals("y")) {
					System.out.println("������ �����մϴ�.");
					break;
				}
			} else {
				System.out.println("1~3�� �߿� �Է����ּ���!!");
			}

		}

	}

}
