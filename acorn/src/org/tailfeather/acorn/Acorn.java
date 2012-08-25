package org.tailfeather.acorn;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Acorn {
	private static final Logger LOGGER = Logger.getLogger(Acorn.class.getName());
	private static final int OUTPUT_BPS = 300;

	public static void main(String[] args) throws Exception {
		LogConfig.configureRootLogger();

		// Wedge in a slow output stream to standard output
		System.setOut(new PrintStream(new SlowOutputStream(System.out, OUTPUT_BPS)));

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
			ConsoleUtils.clear();
			System.console().writer().write(Messages.getMessage("org/tailfeather/acorn/main.txt"));
			System.console().flush();

			if (true)
				return 0;
		}

		return 0;
	}
}
