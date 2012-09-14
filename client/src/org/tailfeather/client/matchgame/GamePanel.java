package org.tailfeather.client.matchgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private long timeLimitSeconds = 30;
	private long startMillis = -1;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Rectangle r = g.getClipBounds();

		if (startMillis != -1) {
			long deltaMillis = System.currentTimeMillis() - startMillis;
			long limitMillis = timeLimitSeconds * 1000;

			float ratio = (float) deltaMillis / (float) limitMillis;
			int fillHeight = (int) ((float) r.height * ratio);

			g2.setColor(Color.PINK);
			g2.fillRect(0, r.height - fillHeight, r.width, r.height);
		}
	}

	public void start() {
		startMillis = System.currentTimeMillis();
	}
}
