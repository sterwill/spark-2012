package org.tailfeather.client.matchgame;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MatchGameFrame {
	private final static Logger LOGGER = Logger.getLogger(MatchGameFrame.class.getName());

	private final static int CHOICES = 4;

	private final Random random = new Random();
	private final JFrame frame;

	private static enum GameImage {
		Cat, Fish, Pear, Airplane, Banana, Car, Dog, Heart, Square, Monkey, Star, Phone, Hand, Turtle
	}

	private final Map<GameImage, Image> images = new HashMap<GameImage, Image>();
	private final Map<GameImage, GameImage> matches = new HashMap<GameImage, GameImage>();

	private final ImageIcon targetImageIcon;
	private final JPanel choicePanel;

	public MatchGameFrame() throws IOException {
		frame = new JFrame("Tail Feather Authorization Challenge");
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		images.put(GameImage.Cat, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Fish, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Pear, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Airplane, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Banana, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Car, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Dog, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Heart, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Square, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Monkey, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Star, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Phone, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Hand, ImageIO.read(new File("images/x.png")));
		images.put(GameImage.Turtle, ImageIO.read(new File("images/x.png")));

		matches.put(GameImage.Cat, GameImage.Fish);
		matches.put(GameImage.Pear, GameImage.Airplane);
		matches.put(GameImage.Banana, GameImage.Car);
		matches.put(GameImage.Dog, GameImage.Heart);
		matches.put(GameImage.Square, GameImage.Monkey);
		matches.put(GameImage.Star, GameImage.Phone);
		matches.put(GameImage.Hand, GameImage.Turtle);

		BoxLayout vBox = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
		frame.getContentPane().setLayout(vBox);
		targetImageIcon = new ImageIcon();
		JLabel targetLabel = new JLabel(targetImageIcon);
		frame.getContentPane().add(targetLabel);

		choicePanel = new JPanel(new BoxLayout(vBox.getTarget(), BoxLayout.X_AXIS));
	}

	private void setTargetImage(GameImage image) {
		targetImageIcon.setImage(images.get(image));
	}

	private void setChoiceImages(GameImage[] choices) {
		choicePanel.removeAll();
		for (GameImage c : choices) {
			Image i = images.get(c);
			choicePanel.add(new JLabel(new ImageIcon(i)));
		}
	}

	private GameImage[] getChoices(GameImage image) {
		int num = CHOICES;
		List<GameImage> choices = new ArrayList<GameImage>();
		choices.add(matches.get(image));
		num++;

		List<GameImage> allImages = new ArrayList<GameImage>(images.keySet());
		allImages.remove(image);
		Collections.shuffle(allImages, random);

		while (num-- > 0) {
			choices.add(allImages.remove(0));
		}

		Collections.shuffle(choices, random);
		return (GameImage[]) choices.toArray(new GameImage[choices.size()]);
	}

	public void run() {
		// Initializes toolkit for sounds
		new JFXPanel();

		MediaPlayer mediaPlayer = null;
		try {
			Media hit = new Media(new File("sounds/petrol.mp3").toURI().toString());
			mediaPlayer = new MediaPlayer(hit);
			mediaPlayer.setVolume(.8f);
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			mediaPlayer.play();

			frame.getContentPane().setBackground(Color.BLACK);
			frame.setVisible(true);

			for (GameImage i : matches.keySet()) {
				GameImage match = matches.get(i);
				GameImage[] choices = getChoices(i);

				setTargetImage(i);
				setChoiceImages(choices);

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}
			}

			frame.setVisible(false);
		} finally {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
		}
	}
}
