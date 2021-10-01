import java.util.ArrayList;

import javazoom.jl.player.MP3Player;

public class MusicPlayer {
	
	MP3Player mp3 = new MP3Player();
	ArrayList<Music> musicList = new ArrayList<Music>();
	int numMusic = 0;
	
	
	
	public MusicPlayer() {
		musicList.add(new Music("�������","C:\\Users\\smhrd\\Desktop\\����\\�̴�������Ʈ\\soundeffect\\backgroundmusic.mp3"));
		musicList.add(new Music("��Ÿ","C:\\Users\\smhrd\\Desktop\\����\\�̴�������Ʈ\\soundeffect\\hit.mp3"));
		musicList.add(new Music("Ȩ��","C:\\Users\\smhrd\\Desktop\\����\\�̴�������Ʈ\\soundeffect\\homerun.mp3"));
		musicList.add(new Music("��Ʈ����ũ","C:\\Users\\smhrd\\Desktop\\����\\�̴�������Ʈ\\soundeffect\\strike.mp3"));
	}
	
	public void play(int numMusic) {
		
		if(!mp3.isPlaying()) {
			mp3.play(musicList.get(numMusic).getPath());
		
		}
		
	}
	
	public void stop() {
		if(mp3.isPlaying()) {
			mp3.stop();
		}
		
	}

}
