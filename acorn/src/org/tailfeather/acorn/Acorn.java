package org.tailfeather.acorn;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Acorn {
	private static final Logger LOGGER = Logger.getLogger(Acorn.class.getName());
	private static final int OUTPUT_BPS = 100;
	private static final String MAIN_PROMPT = "SYSTEM {) ";

	public static final SlowConsole CONSOLE = new SlowConsole(OUTPUT_BPS);

	public static void main(String[] args) throws Exception {
		LogConfig.configureRootLogger();

		try {
			System.exit(new Acorn().run(args));
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Uncaught exception", e);
			e.printStackTrace();
		}
	}

	public Acorn() {
	}

	public int run(String args[]) throws Exception {
		// CONSOLE.clear();
		CONSOLE.print(Messages.getMessage("org/tailfeather/acorn/main.txt"));
		CONSOLE.flush();

		boolean stop = false;
		while (!stop) {
			CONSOLE.eraseLine();
			final String line = CONSOLE.readLine(MAIN_PROMPT);
			if (line == null) {
				CONSOLE.println();
				continue;
			}

			final String trim = line.trim();
			if (trim.length() == 0) {
				continue;
			}

			switch (trim) {
			case "exit":
			case "quit":
			case "bye":
				CONSOLE.println();
				CONSOLE.println("Goodbye.");
				stop = true;
				break;
			}
		}

		return 0;
	}
}
