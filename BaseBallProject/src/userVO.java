
public class userVO {
	// ȸ������ �ʵ� id, pw, team, score
		private String id;
		private String pw;
		private String team;
		private int score;
		
		
		@Override
		public String toString() {
			return "���̵� " + id + "\t ���ܸ� " + team + "\t ���� " + score ;
		}

		public userVO(String id, String pw) {
			super();
			this.id = id;
			this.pw = pw;
		}

		
		public userVO(String id, String pw, String team) {
			super();
			this.id = id;
			this.pw = pw;
			this.team = team;
		}
		

		public userVO(String id, String team, int score) {
			super();
			this.id = id;
			this.team = team;
			this.score = score;
		}

		public userVO(String id, String pw, String team, int score) {
			super();
			this.id = id;
			this.pw = pw;
			this.team = team;
			this.score = score;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPw() {
			return pw;
		}

		public void setPw(String pw) {
			this.pw = pw;
		}

		public String getTeam() {
			return team;
		}

		public void setTeam(String team) {
			this.team = team;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}
		
		
		
}
