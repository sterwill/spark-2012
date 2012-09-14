package org.tailfeather.client.matchgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.Timer;

public class MatchGameFrame {
	private final static Logger LOGGER = Logger.getLogger(MatchGameFrame.class.getName());

	private final static int CHOICES = 4;
	private final static Font SCORE_FONT = new Font(null, Font.BOLD, 30);
	private final static Font CHOICE_FONT = new Font(null, Font.BOLD, 60);

	private final Random random = new Random();
	private final JFrame frame;

	private static enum GameImage {
		Cat, Fish, Pear, Airplane, Banana, Car, Dog, Heart, Square, Monkey, Star, Phone, Hand, Turtle
	}

	private final Map<GameImage, Image> images = new HashMap<GameImage, Image>();
	private final Map<GameImage, GameImage> matches = new HashMap<GameImage, GameImage>();

	private final Image blankImage;
	private final ImageIcon targetImageIcon;
	private final ImageIcon[] choiceImageIcons = new ImageIcon[CHOICES];

	private GamePanel gamePanel;

	public MatchGameFrame() throws IOException {
		frame = new JFrame("Tail Feather Authorization Challenge");
		frame.setUndecorated(true);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);

		blankImage = ImageIO.read(new File("images/match/_blank.png"));

		images.put(GameImage.Cat, ImageIO.read(new File("images/match/cat.png")));
		images.put(GameImage.Fish, ImageIO.read(new File("images/match/fish.png")));
		images.put(GameImage.Pear, ImageIO.read(new File("images/match/pear.png")));
		images.put(GameImage.Airplane, ImageIO.read(new File("images/match/airplane.png")));
		images.put(GameImage.Banana, ImageIO.read(new File("images/match/banana.png")));
		images.put(GameImage.Car, ImageIO.read(new File("images/match/car.png")));
		images.put(GameImage.Dog, ImageIO.read(new File("images/match/dog.png")));
		images.put(GameImage.Heart, ImageIO.read(new File("images/match/heart.png")));
		images.put(GameImage.Square, ImageIO.read(new File("images/match/square.png")));
		images.put(GameImage.Monkey, ImageIO.read(new File("images/match/monkey.png")));
		images.put(GameImage.Star, ImageIO.read(new File("images/match/star.png")));
		images.put(GameImage.Phone, ImageIO.read(new File("images/match/phone.png")));
		images.put(GameImage.Hand, ImageIO.read(new File("images/match/hand.png")));
		images.put(GameImage.Turtle, ImageIO.read(new File("images/match/turtle.png")));

		matches.put(GameImage.Cat, GameImage.Fish);
		matches.put(GameImage.Pear, GameImage.Airplane);
		matches.put(GameImage.Banana, GameImage.Car);
		matches.put(GameImage.Dog, GameImage.Heart);
		matches.put(GameImage.Square, GameImage.Monkey);
		matches.put(GameImage.Star, GameImage.Phone);
		matches.put(GameImage.Hand, GameImage.Turtle);

		gamePanel = new GamePanel();
		gamePanel.setOpaque(false);
		frame.getContentPane().add(gamePanel);

		final BoxLayout vBox = new BoxLayout(gamePanel, BoxLayout.Y_AXIS);
		gamePanel.setLayout(vBox);

		JPanel targetImagePanel = new JPanel();
		targetImagePanel.setOpaque(false);

		JPanel scorePanel = new JPanel();
		scorePanel.setOpaque(false);
		BoxLayout scoreBox = new BoxLayout(scorePanel, BoxLayout.X_AXIS);
		scorePanel.setLayout(scoreBox);

		JLabel scoreLabel = new JLabel("Foo");
		scoreLabel.setAlignmentX(0);
		scoreLabel.setForeground(Color.RED);
		scoreLabel.setOpaque(false);
		scoreLabel.setFont(SCORE_FONT);

		JLabel instructionsLabel = new JLabel("Instructions here");
		instructionsLabel.setAlignmentX(1);
		instructionsLabel.setForeground(Color.WHITE);
		instructionsLabel.setOpaque(false);
		instructionsLabel.setFont(SCORE_FONT);

		scorePanel.add(scoreLabel);
		scorePanel.add(Box.createHorizontalGlue());
		scorePanel.add(instructionsLabel);

		gamePanel.add(scorePanel);
		gamePanel.add(Box.createVerticalGlue());
		gamePanel.add(targetImagePanel);
		gamePanel.add(Box.createVerticalGlue());

		// Target image
		targetImageIcon = new ImageIcon(blankImage);
		JLabel targetLabel = new JLabel(targetImageIcon);
		targetImagePanel.add(targetLabel);

		gamePanel.add(Box.createVerticalGlue());

		JPanel choicePanel = new JPanel();
		choicePanel.setOpaque(false);
		BoxLayout hBox = new BoxLayout(choicePanel, BoxLayout.X_AXIS);
		choicePanel.setLayout(hBox);
		gamePanel.add(choicePanel);

		// Choices
		choicePanel.add(Box.createHorizontalGlue());
		for (int i = 0; i < choiceImageIcons.length; i++) {
			choiceImageIcons[i] = new ImageIcon(blankImage);

			JPanel thisChoicePanel = new JPanel();
			thisChoicePanel.setOpaque(false);

			BoxLayout vChoiceBox = new BoxLayout(thisChoicePanel, BoxLayout.Y_AXIS);
			thisChoicePanel.setLayout(vChoiceBox);

			JLabel imageLabel = new JLabel(choiceImageIcons[i]);
			imageLabel.setAlignmentX(.5f);
			imageLabel.setAlignmentY(.5f);
			thisChoicePanel.add(imageLabel);

			thisChoicePanel.add(Box.createVerticalGlue());

			JLabel numberLabel = new JLabel(Integer.toString(i));
			numberLabel.setFont(CHOICE_FONT);
			numberLabel.setForeground(Color.YELLOW);
			numberLabel.setOpaque(false);
			numberLabel.setAlignmentX(.5f);
			numberLabel.setAlignmentY(.5f);
			thisChoicePanel.add(numberLabel);

			choicePanel.add(thisChoicePanel);

			choicePanel.add(Box.createHorizontalGlue());
		}

		gamePanel.doLayout();
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

			frame.setVisible(true);

			Timer timer = new Timer(100, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gamePanel.repaint();
				}
			});
			timer.start();

			boolean i = true;
			while (i) {
				for (GameImage target : matches.keySet()) {
					GameImage match = matches.get(target);
					GameImage[] choices = getChoices(target);
					setIcons(target, choices);

					gamePanel.start();

					Thread.sleep(10000);
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

	private void setIcons(GameImage target, GameImage[] choices) {
		targetImageIcon.setImage(images.get(target));
		for (int i = 0; i < choiceImageIcons.length; i++) {
			choiceImageIcons[i].setImage(images.get(choices[i]));
		}
		frame.repaint();
	}
}
