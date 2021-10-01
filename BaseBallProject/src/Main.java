import java.util.Scanner;
import javazoom.jl.player.MP3Player;


public class Main {

	public static void main(String[] args) throws ClassNotFoundException {
//Ŀ��
		Scanner sc = new Scanner(System.in);
		userDAO dao = new userDAO();
		PlayerDAO daoP = new PlayerDAO();
		
		MP3Player mp3 = new MP3Player();

		MusicPlayer player = new MusicPlayer();
		
		Music music;

		while (true) {

			player.play(0);
			System.out.print("[1]�α���  [2]ȸ������  [3]���� >> ");
			int select = sc.nextInt();

			if (select == 1) {

				// login �����ϱ�

				// login

				String id = dao.login();
				while (true) {
					System.out.print("[1]������  [2]��ŷ����  [3]����ȭ�� >> ");
					int select2 = sc.nextInt();
					if (select2 == 1) { // ������
						player.stop();
						daoP.game(id, daoP.enemyPick(id));

					} else if (select2 == 2) {
						// ranking

						dao.showRanking(dao.getRanking(), id);

					} else if (select2 == 3) {
						System.out.println("�������� ���ư��ϴ�.");
						break;
					} else {
						System.out.println("1~3�� �߿� �Է����ּ���!!");
					}
				}

			}

			else if (select == 2) {
				// signup
				// playerInput(�������)
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
//						System.out.println("ȸ������ ����...");
//					}

				// show_playerList

			} else if (select == 3) {
				System.out.print("��¥ �����Ͻðڽ��ϱ�?�� >> (y/n) >> ");
				String answer = sc.next();
				if (answer.equals("y")) {
					player.stop();
					System.out.println("������ �����մϴ�!");
					break;
				}
			} else {
				System.out.println("1~3�� �߿� �Է����ּ���!!");
			}

		}

	}

}
