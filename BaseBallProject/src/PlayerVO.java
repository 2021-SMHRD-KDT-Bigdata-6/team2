
public class PlayerVO {
	
	private String name;
	private int num;
	private int stat;
	
	
	public PlayerVO(int num, String name, int stat) {
		this.name = name;
		this.num = num;
		this.stat = stat;
	}
	
	public PlayerVO(String name, int stat) {
		this.name = name;
		this.stat = stat;
	}

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getNum() {
		return num;
	}


	public void setNum(int num) {
		this.num = num;
	}


	public int getStat() {
		return stat;
	}


	public void setStat(int stat) {
		this.stat = stat;
	}
	
	public String toString() {
		return "이름 : "+name+"\t능력치 : "+stat;
	
	}
	
	
	
	

}
