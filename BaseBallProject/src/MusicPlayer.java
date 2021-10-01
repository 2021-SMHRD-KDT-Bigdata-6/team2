import java.util.ArrayList;

import javazoom.jl.player.MP3Player;

public class MusicPlayer {
	
	MP3Player mp3 = new MP3Player();
	ArrayList<Music> musicList = new ArrayList<Music>();
	int numMusic = 0;
	
	
	
	public MusicPlayer() {
		musicList.add(new Music("배경음악","play\\backgroundmusic.mp3"));
		musicList.add(new Music("안타","play\\hit.mp3"));
		musicList.add(new Music("홈런","play\\homerun.mp3"));
		musicList.add(new Music("스트라이크","play\\strike.mp3"));
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
