package org.tailfeather.acorn;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundUtils {
	private static final Logger LOGGER = Logger.getLogger(SoundUtils.class.getName());

	public static void playSound(String file) {
		if (file != null) {
			try {
				Clip clip = AudioSystem.getClip();
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(file));
				clip.open(inputStream);
				clip.start();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Error playing sound " + file, e);
			}
		}
	}
}
