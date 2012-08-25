package org.tailfeather.acorn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Screensaver {
	private final String blankLine;
	private final List<String> lines = new ArrayList<String>();
	private final Random random = new Random();

	public Screensaver() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ConsoleUtils.getColumns(); i++)
			sb.append(" ");
		blankLine = sb.toString();
	}

	public void run() throws Exception {
		clearLines();
		while (true) {
			dissolveLine(0);
			print();
			addLine();
			Thread.sleep(100);
		}
	}

	private void dissolveLine(int line) {
		StringBuilder sb = new StringBuilder(lines.get(line));
		for (int i = 0; i < sb.length(); i++) {
			if (random.nextInt(ConsoleUtils.getColumns() / 2) == 0) {
				char c = sb.charAt(i);
				switch (c) {
				case '.':
				case '_':
					c = ' ';
					break;
				case '{':
					c = '.';
					break;
				case 'D':
					c = '_';
					break;
				}
				sb.setCharAt(i, c);
			}
		}
		lines.set(line, sb.toString());
	}

	private void clearLines() {
		lines.clear();
		for (int i = 0; i < ConsoleUtils.getLines(); i++) {
			lines.add(blankLine);
		}
	}

	private void print() {
		ConsoleUtils.clear();
		for (int i = lines.size() - 1; i >= 0; i--) {
			System.out.println(lines.get(i));
		}
	}

	private void addLine() {
		StringBuilder line = new StringBuilder(blankLine);

		if (random.nextInt(6) == 0) {
			int pos = random.nextInt(ConsoleUtils.getColumns() - 1);
			line.setCharAt(pos, '{');
			line.setCharAt(pos + 1, 'D');
		}

		lines.add(line.toString());
		while (lines.size() > ConsoleUtils.getLines()) {
			String l = lines.remove(0);
			StringBuilder sb = new StringBuilder(lines.get(0));
			for (int i = 0; i < l.length(); i++) {
				if (l.charAt(i) != ' ') {
					sb.setCharAt(i, l.charAt(i));
				}
			}
			lines.set(0, sb.toString());
		}
	}
}
