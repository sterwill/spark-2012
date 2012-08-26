package org.tailfeather.acorn;

public class SlowConsole {
	private static final String CLEAR = "\033[2J";
	private static final String HOME = "\033[H";
	private static final String CLEAR_LINE = "\033[2K";

	private final long period;
	private long lastPrinted = -1;

	public SlowConsole(int bytesPerSecond) {
		this.period = (long) (1000f / bytesPerSecond);
	}

	public void flush() {
		System.console().flush();
	}

	public void clear() {
		print(HOME);
		print(CLEAR);
		flush();
	}

	public void eraseLine() {
		print(CLEAR_LINE);
		flush();
	}

	public void print(char c) {
		slow();
		System.console().writer().write(c);
		flush();
	}

	public void print(String s) {
		int i = 0;
		int len = s.length();
		while (i < len) {
			int c = s.codePointAt(i);
			i += Character.charCount(c);
			print((char) c);
		}
	}

	public void println() {
		println("");
	}

	public void println(String s) {
		print(s);
		print(System.getProperty("line.separator"));
	}

	public String readLine(String prompt, Object... args) {
		return System.console().readLine(prompt, args);
	}

	private void slow() {
		final long wait = lastPrinted + period - System.currentTimeMillis();
		if (wait <= 0) {
			lastPrinted = System.currentTimeMillis();
			return;
		}

		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
