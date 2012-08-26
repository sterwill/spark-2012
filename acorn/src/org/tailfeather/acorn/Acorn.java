package org.tailfeather.acorn;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Acorn {
	private static final Logger LOGGER = Logger.getLogger(Acorn.class.getName());
	private static final int TIMEOUT_SECONDS = 30;

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
		root: while (true) {
			Console.clear();
			Console.print(Messages.getMessage("org/tailfeather/acorn/motd.txt"));
			Console.flush();

			prompt: while (true) {
				final String line;
				try {
					line = Console.readLine(Messages.getMessage("org/tailfeather/acorn/main-prompt.txt"),
							TIMEOUT_SECONDS, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "Interrupted while prompting", e);
					continue;
				} catch (TimeoutException e) {
					break;
				}

				if (line == null) {
					Console.printLine();
					continue;
				}

				final String[] tokens = Command.parse(line);
				if (tokens.length == 0) {
					continue;
				}

				switch (tokens[0]) {
				case "exit":
					continue root;
				case "help":
					printHelp();
					break;
				case "ps":
				case "who":
				case "ls":
				case "dir":
				case "last":
				case "rm":
					printErrorLine("Access to that command is restricted");
					break;
				default:
					printErrorLine(MessageFormat.format("Unrecognized command ''{0}'', please try again", tokens[0]));
					break;
				}
			}
		}
	}

	private void printErrorLine(String error) {
		Console.printLine();
		Console.printRedLine(error);
		Console.printLine();
	}

	private void printHelp() {
		Console.printLine();
		Console.printLine("help       prints this text");
		Console.printLine("register   begins the registration process");
		Console.printLine();
	}
}
