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
		boolean stop = false;
		while (!stop) {
			CONSOLE.clear();
			CONSOLE.print(Messages.getMessage("org/tailfeather/acorn/main.txt"));
			CONSOLE.flush();

			final String line = CONSOLE.readLine(MAIN_PROMPT);
			return 0;
		}

		return 0;
	}
}
