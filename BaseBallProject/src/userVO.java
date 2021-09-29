
public class userVO {
	// 회원정보 필드 id, pw, team, score
		private String id;
		private String pw;
		private String team;
		private int score;
		
		
		@Override
		public String toString() {
			return "userVO [id=" + id + ", team=" + team + ", score=" + score + "]";
		}

		public userVO(String id, String pw) {
			super();
			this.id = id;
			this.pw = pw;
		}

		public userVO(String id, String pw, String team, int core) {
			super();
			this.id = id;
			this.pw = pw;
			this.team = team;
			this.score = core;
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

		public int getCore() {
			return score;
		}

		public void setCore(int core) {
			this.score = core;
		}
		
		
		
}
