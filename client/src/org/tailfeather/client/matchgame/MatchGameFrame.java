package org.tailfeather.client.matchgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.tailfeather.client.SoundUtils;
import org.tailfeather.client.matchgame.MatchGame.Symbol;

public class MatchGameFrame {
	private final static Logger LOGGER = Logger.getLogger(MatchGameFrame.class.getName());

	private final static Color WIN_COLOR = new Color(0f, 1f, 0f, .25f);
	private final static Color LOSE_COLOR = new Color(1f, 0f, 0f, 1f);

	private final static Font SCORE_AND_INSTRUCTIONS_FONT = new Font("Monospaced", Font.BOLD, 30);
	private final static Font CHOICE_FONT = new Font("Monospaced", Font.BOLD, 70);

	private static final int GAME_DURATION_SECONDS = 30;
	private static final int GAME_INCORRECT_PENALTY_SECONDS = 10;

	private final JFrame frame;

	private final Map<Symbol, Image> images = new HashMap<Symbol, Image>();

	private final Image blankImage;
	private final ImageIcon quizImageIcon;
	private final ImageIcon[] choiceImageIcons = new ImageIcon[MatchGame.CHOICES];

	private MatchGame game;
	private GamePanel gamePanel;

	private JLabel scoreLabel;

	public MatchGameFrame() throws IOException {
		frame = new JFrame("Tail Feather Authorization Challenge");
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);

		blankImage = ImageIO.read(new File("images/match/_blank.png"));

		images.put(Symbol.Cat, ImageIO.read(new File("images/match/cat.png")));
		images.put(Symbol.Fish, ImageIO.read(new File("images/match/fish.png")));
		images.put(Symbol.Pear, ImageIO.read(new File("images/match/pear.png")));
		images.put(Symbol.Airplane, ImageIO.read(new File("images/match/airplane.png")));
		images.put(Symbol.Banana, ImageIO.read(new File("images/match/banana.png")));
		images.put(Symbol.Car, ImageIO.read(new File("images/match/car.png")));
		images.put(Symbol.Dog, ImageIO.read(new File("images/match/dog.png")));
		images.put(Symbol.Heart, ImageIO.read(new File("images/match/heart.png")));
		images.put(Symbol.Square, ImageIO.read(new File("images/match/square.png")));
		images.put(Symbol.Monkey, ImageIO.read(new File("images/match/monkey.png")));
		images.put(Symbol.Star, ImageIO.read(new File("images/match/star.png")));
		images.put(Symbol.Phone, ImageIO.read(new File("images/match/phone.png")));
		images.put(Symbol.Hand, ImageIO.read(new File("images/match/hand.png")));
		images.put(Symbol.Turtle, ImageIO.read(new File("images/match/turtle.png")));

		game = new MatchGame(GAME_DURATION_SECONDS, GAME_INCORRECT_PENALTY_SECONDS);

		gamePanel = new GamePanel(game, WIN_COLOR, LOSE_COLOR);
		gamePanel.setBackground(Color.BLACK);
		gamePanel.setOpaque(true);
		frame.getContentPane().add(gamePanel);

		final BoxLayout vBox = new BoxLayout(gamePanel, BoxLayout.Y_AXIS);
		gamePanel.setLayout(vBox);

		JPanel quizImagePanel = new JPanel();
		quizImagePanel.setOpaque(false);

		JPanel scorePanel = new JPanel();
		scorePanel.setOpaque(false);
		BoxLayout scoreBox = new BoxLayout(scorePanel, BoxLayout.X_AXIS);
		scorePanel.setLayout(scoreBox);

		scoreLabel = new JLabel();
		scoreLabel.setBorder(new EmptyBorder(20, 20, 0, 0));
		scoreLabel.setAlignmentX(0);
		scoreLabel.setForeground(Color.YELLOW);
		scoreLabel.setOpaque(false);
		scoreLabel.setFont(SCORE_AND_INSTRUCTIONS_FONT);

		JLabel instructionsLabel = new JLabel("Choose the matching symbol (quickly)");
		instructionsLabel.setBorder(new EmptyBorder(20, 0, 0, 20));
		instructionsLabel.setAlignmentX(1);
		instructionsLabel.setForeground(Color.RED);
		instructionsLabel.setOpaque(false);
		instructionsLabel.setFont(SCORE_AND_INSTRUCTIONS_FONT);

		scorePanel.add(scoreLabel);
		scorePanel.add(Box.createHorizontalGlue());
		scorePanel.add(instructionsLabel);

		gamePanel.add(scorePanel);
		gamePanel.add(Box.createVerticalGlue());
		gamePanel.add(quizImagePanel);
		gamePanel.add(Box.createVerticalGlue());

		// Target image
		quizImageIcon = new ImageIcon(blankImage);
		JLabel quizLabel = new JLabel(quizImageIcon);
		quizImagePanel.add(quizLabel);

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

			JLabel numberLabel = new JLabel(Integer.toString(i + 1));
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

		// Keys

		gamePanel.getInputMap().put(KeyStroke.getKeyStroke("1"), "choose1");
		gamePanel.getInputMap().put(KeyStroke.getKeyStroke("2"), "choose2");
		gamePanel.getInputMap().put(KeyStroke.getKeyStroke("3"), "choose3");
		gamePanel.getInputMap().put(KeyStroke.getKeyStroke("4"), "choose4");

		gamePanel.getActionMap().put("choose1", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.choose(1);
			}
		});
		gamePanel.getActionMap().put("choose2", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.choose(2);
			}
		});
		gamePanel.getActionMap().put("choose3", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.choose(3);
			}
		});
		gamePanel.getActionMap().put("choose4", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.choose(4);
			}
		});
	}

	public boolean run() {
		MediaPlayer mediaPlayer = null;
		try {
			new JFXPanel();

			Media song = new Media(new File("sounds/petrol.mp3").toURI().toString());
			mediaPlayer = new MediaPlayer(song);
			mediaPlayer.setVolume(.8f);
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			mediaPlayer.play();

			updateScore();

			frame.setVisible(true);

			GameStatusChangedListener statusListener = new GameStatusChangedListener() {
				@Override
				public void onGameStatusChanged(GameStatus oldStatus, GameStatus newStatus) {
					updateIcons();
				}
			};
			game.addStatusChangedListener(statusListener);

			ChoiceListener choiceListener = new ChoiceListener() {
				@Override
				public void onChoice(boolean correct) {
					updateScore();

					if (game.getStatus() == GameStatus.PLAYING) {
						if (correct) {
							SoundUtils.playSound("sounds/correct.mp3");
						} else {
							SoundUtils.playSound("sounds/buzzer.mp3");
						}
					} else if (game.getStatus() == GameStatus.LOSE) {
						SoundUtils.playSound("sounds/buzzer.mp3");
					} else if (game.getStatus() == GameStatus.WIN) {
						SoundUtils.playSound("sounds/electric-sweep.mp3");
					}

					updateIcons();
					frame.getContentPane().repaint();
				}
			};
			game.addChoiceListener(choiceListener);

			game.start();

			while (game.getStatus() == GameStatus.PLAYING) {
				game.tick();
				frame.getContentPane().repaint();
				Thread.sleep(20);
			}

			frame.getContentPane().repaint();
			Thread.sleep(3000);

			game.removeChoiceListener(choiceListener);
			game.removeStatusChangedListener(statusListener);
			game.stop();

			frame.setVisible(false);

		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE, "Error in match game", t);
		} finally {
			frame.setVisible(false);
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
		}

		return game.getStatus() == GameStatus.WIN;
	}

	protected void updateIcons() {
		Symbol quizSymbol = game.getQuizSymbol();
		Symbol[] choiceSymbols = game.getChoiceSymbols();

		quizImageIcon.setImage(images.get(quizSymbol));

		for (int i = 0; i < choiceSymbols.length; i++) {
			choiceImageIcons[i].setImage(images.get(choiceSymbols[i]));
		}
		frame.getContentPane().repaint();
	}

	protected void updateScore() {
		scoreLabel.setText(String.format("Correct answers: %d / %d", game.getNumCorrect(), game.getQuizSymbolCount()));
	}
}
