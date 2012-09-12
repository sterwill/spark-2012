package org.tailfeather.client.matchgame;

import java.awt.Frame;
import java.io.File;
import java.util.logging.Logger;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.JFrame;

public class MatchGameFrame {
	private final static Logger LOGGER = Logger.getLogger(MatchGameFrame.class.getName());

	private final JFrame frame;

	public MatchGameFrame() {
		frame = new JFrame("Tail Feather Authorization Challenge");
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		// Toolkit tk = Toolkit.getDefaultToolkit();
		// int xSize = ((int) tk.getScreenSize().getWidth());
		// int ySize = ((int) tk.getScreenSize().getHeight());
		// frame.setSize(xSize, ySize);
	}

	public void run() {
		// Initializes toolkit for sounds
		new JFXPanel();

		MediaPlayer mediaPlayer = null;
		try {
			Media hit = new Media(new File("sounds/petrol.mp3").toURI().toString());
			mediaPlayer = new MediaPlayer(hit);
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			mediaPlayer.play();

			frame.setVisible(true);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			frame.setVisible(false);
		} finally {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
		}

	}
}
