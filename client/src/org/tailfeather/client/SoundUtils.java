package org.tailfeather.client;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundUtils {
	private static final Logger LOGGER = Logger.getLogger(SoundUtils.class.getName());

	private static volatile boolean initialized;

	public static void playSound(final String file) {
		try {
			if (!initialized) {
				// Initializes toolkit for sounds
				new JFXPanel();
				initialized = true;
			}
			new Thread(new Runnable() {
				private volatile boolean playing = true;

				@Override
				public void run() {
					Media sound = new Media(new File(file).toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(sound);
					mediaPlayer.setOnEndOfMedia(new Runnable() {
						@Override
						public void run() {
							playing = false;
						}
					});

					mediaPlayer.play();

					while (playing) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
						}
					}
				}
			}).start();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error playing sound " + file, e);
		}
	}

	public static void playSoundOld(String file) {
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
