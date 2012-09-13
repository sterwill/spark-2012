package org.tailfeather.client.matchgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Label;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MatchGameFrame {
	private final static Logger LOGGER = Logger.getLogger(MatchGameFrame.class.getName());

	private final static int CHOICES = 4;
	private final static Font FONT = new Font(null, Font.BOLD, 60);

	private final Random random = new Random();
	private final JFrame frame;

	private static enum GameImage {
		Cat, Fish, Pear, Airplane, Banana, Car, Dog, Heart, Square, Monkey, Star, Phone, Hand, Turtle
	}

	private final Map<GameImage, Image> images = new HashMap<GameImage, Image>();
	private final Map<GameImage, GameImage> matches = new HashMap<GameImage, GameImage>();

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
	}

	private void show(GameImage targetImage, GameImage[] choices) {
		frame.getContentPane().removeAll();
		frame.getContentPane().revalidate();

		JPanel targetImagePanel = new JPanel();
		targetImagePanel.setOpaque(false);
		targetImagePanel.setSize(512, 512);

		frame.getContentPane().add(Box.createVerticalGlue());
		frame.getContentPane().add(targetImagePanel);
		frame.getContentPane().add(Box.createVerticalGlue());

		ImageIcon targetImageIcon = new ImageIcon();
		JLabel targetLabel = new JLabel(targetImageIcon);
		targetImagePanel.add(targetLabel);

		frame.getContentPane().add(Box.createVerticalGlue());

		JPanel choicePanel = new JPanel();
		choicePanel.setOpaque(false);
		choicePanel.setBackground(Color.RED);
		BoxLayout hBox = new BoxLayout(choicePanel, BoxLayout.X_AXIS);
		choicePanel.setLayout(hBox);
		frame.getContentPane().add(choicePanel);

		// Target image
		targetImageIcon.setImage(images.get(targetImage));

		// Choices
		choicePanel.add(Box.createHorizontalGlue());
		choicePanel.setBackground(Color.RED);

		int choiceNumber = 1;
		for (GameImage c : choices) {
			Image i = images.get(c);

			JPanel thisChoicePanel = new JPanel();
			thisChoicePanel.setOpaque(false);

			BoxLayout vBox = new BoxLayout(thisChoicePanel, BoxLayout.Y_AXIS);
			thisChoicePanel.setLayout(vBox);

			JLabel imageLabel = new JLabel(new ImageIcon(i));
			imageLabel.setAlignmentX(.5f);
			imageLabel.setAlignmentY(.5f);
			thisChoicePanel.add(imageLabel);

			thisChoicePanel.add(Box.createVerticalGlue());

			Label numberLabel = new Label(Integer.toString(choiceNumber++));
			numberLabel.setFont(FONT);
			numberLabel.setBackground(Color.BLACK);
			numberLabel.setForeground(Color.YELLOW);
			numberLabel.setAlignment(Label.CENTER);
			thisChoicePanel.add(numberLabel);

			choicePanel.add(thisChoicePanel);

			choicePanel.add(Box.createHorizontalGlue());
		}
		frame.getContentPane().revalidate();
	}

	private GameImage[] getChoices(GameImage image) {
		int num = CHOICES;
		List<GameImage> choices = new ArrayList<GameImage>();
		choices.add(matches.get(image));
		num--;

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
				// GameImage match = matches.get(i);
				GameImage[] choices = getChoices(i);

				show(i, choices);

				for (int x = 100; x > 0; x--) {
					frame.setOpacity(1f / x);
					Thread.sleep(10);
				}
			}

			frame.setVisible(false);
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE, "Error doing game", t);
		} finally {
			frame.setVisible(false);
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
		}
	}
}
