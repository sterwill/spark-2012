package org.tailfeather.client.matchgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = -1306707873439226022L;
	private static final Font WIN_LOSE_FONT = new Font(null, Font.BOLD, 120);

	private final MatchGame game;
	private final Color bgColor;
	private final Color loseColor;
	private final Color winColor;

	public GamePanel(MatchGame game, Color bgColor, Color winColor, Color loseColor) {
		this.game = game;
		this.bgColor = bgColor;
		this.winColor = winColor;
		this.loseColor = loseColor;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Rectangle clipBounds = g.getClipBounds();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		paintBackground(g2, clipBounds);

		switch (game.getStatus()) {
		case LOSE:
		case WIN:
			paintWinLoseText(g2, clipBounds);
			break;
		}
	}

	private void paintWinLoseText(Graphics2D g2, Rectangle clipBounds) {
		int x = clipBounds.width / 2;
		int y = clipBounds.height / 2;

		String s;
		Color c;
		if (game.getStatus() == GameStatus.LOSE) {
			s = "Access Denied";
			c = Color.RED;
		} else {
			s = "Access Granted";
			c = Color.GREEN;
		}

		Rectangle2D stringBounds = WIN_LOSE_FONT.getStringBounds(s, g2.getFontRenderContext());

		x -= stringBounds.getWidth() / 2;
		y += stringBounds.getHeight() / 2;

		// Hack: drawing order wrong, so move it up so it doesn't hit the
		// choices
		y -= 30;

		g2.setColor(c);
		g2.setFont(WIN_LOSE_FONT);
		g2.drawString(s, x, y);
	}

	private void paintBackground(Graphics2D g2, Rectangle clipBounds) {
		g2.setColor(bgColor);
		g2.fillRect(0, 0, clipBounds.width, clipBounds.height);

		long remaining = game.getTimeRemainingMillis();

		if (remaining >= 0) {
			float ratio = (float) remaining / (game.getRoundDurationSeconds() * 1000f);
			int fillHeight = (int) Math.round(clipBounds.height * ratio);

			g2.setColor(getColor(ratio));
			g2.fillRect(0, clipBounds.height - fillHeight, clipBounds.width, clipBounds.height);
		}
	}

	private Color getColor(float ratio) {
		if (ratio > 1f) {
			ratio = 1f;
		} else if (ratio < 0f) {
			ratio = 0f;
		}

		float[] winComponents = new float[4];
		winColor.getComponents(winComponents);

		float[] loseComponents = new float[4];
		loseColor.getComponents(loseComponents);

		// Linear interpolation
		float r = (1f - ratio) * loseComponents[0] + ratio * winComponents[0];
		float g = (1f - ratio) * loseComponents[1] + ratio * winComponents[1];
		float b = (1f - ratio) * loseComponents[2] + ratio * winComponents[2];
		float a = (1f - ratio) * loseComponents[3] + ratio * winComponents[3];

		return new Color(r, g, b, a);
	}
}
