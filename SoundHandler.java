import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Made For {@link Pong}
 * @author Joseph
 *
 */
public class SoundHandler {
	
	public static final int BOUNCE = 0;
	public static final int SCORE = 1;
	public static final int VICTORY = 2;
	
	public static SoundHandler handler;
	
	private static String dirPrefix;
	
	private static AudioInputStream bounceIN;
	private static AudioInputStream scoreIN;
	private static AudioInputStream victoryIN;
	
	private static Clip bounceClip;
	private static Clip scoreClip;
	private static Clip victoryClip;
	
	
	public SoundHandler() {
		this("sounds/");
	}
	
	public SoundHandler(String dirPrefixIn) {
		dirPrefix = dirPrefixIn;
		handler = this;
	}
	
	public void playSound(int id) {
		try {
			switch (id) {
				case 0: {
					bounceIN = AudioSystem.getAudioInputStream(new File(dirPrefix + "bounce.wav"));
					bounceClip = AudioSystem.getClip();
					bounceClip.open(bounceIN);
					bounceClip.start();
					break;
				}
				case 1: {
					scoreIN = AudioSystem.getAudioInputStream(new File(dirPrefix + "score.wav"));
					scoreClip = AudioSystem.getClip();
					scoreClip.open(scoreIN);
					scoreClip.start();
					break;
				}
				case 2: {
					victoryIN = AudioSystem.getAudioInputStream(new File(dirPrefix + "victory.wav"));
					victoryClip = AudioSystem.getClip();
					victoryClip.open(victoryIN);
					victoryClip.start();
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("An Error occoured while playing the sound.");
			e.printStackTrace();
		}
	}
	
	public static SoundHandler getNewScoreHandler(String dirPrefix) {
		if (dirPrefix != null) {
			new SoundHandler(dirPrefix);
		} else {
			new SoundHandler();
		}
		return handler;
	}
}