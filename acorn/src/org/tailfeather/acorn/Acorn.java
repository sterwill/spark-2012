package org.tailfeather.acorn;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Acorn {
	private static final Logger LOGGER = Logger.getLogger(Acorn.class.getName());
	private static final int MAIN_PROMPT_TIMEOUT_SECONDS = 30;
	private static final int REGISTER_PROMPT_TIMEOUT_SECONDS = 60;

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
			Console.print(Resources.getMessage("org/tailfeather/acorn/motd.txt"));
			Console.flush();

			prompt: while (true) {
				final String line;
				try {
					line = Console.readLine(Resources.getMessage("org/tailfeather/acorn/main-prompt.txt"),
							MAIN_PROMPT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
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

				final String[] tokens = Acorn.parseCommand(line);
				if (tokens.length == 0) {
					continue;
				}

				switch (tokens[0]) {
				case "exit":
					continue root;
				case "help":
					printHelp();
					break;
				case "register":
					register();
					continue root;
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

	private boolean register() {
		Console.print(Resources.getMessage("org/tailfeather/acorn/register-instructions.txt"));
		Console.flush();

		Map<String, String> info = new HashMap<String, String>();
		info.put("Name", null);
		info.put("Dog's Name", null);
		info.put("E-mail Address", null);

		while (true) {
			// Gather info
			if (!getRegistrationInfo(info)) {
				// cancel or timeout
				return false;
			}

			// Summarize
			Console.print(Resources.getMessage("org/tailfeather/acorn/register-verify.txt"));
			for (String key : info.keySet()) {
				Console.printLine("  " + key + ": " + info.get(key));
			}
			Console.printLine();

			// Verify

		}

		return true;
	}

	private boolean verify() {
		while (true) {
			String line;
			try {
				line = Console.readLine(Resources.getMessage("org/tailfeather/acorn/register-verify-prompt.txt"),
						REGISTER_PROMPT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				LOGGER.log(Level.WARNING, "Interrupted while prompting for register", e);
				continue;
			} catch (TimeoutException e) {
				return false;
			}
		}
	}

	private boolean getRegistrationInfo(Map<String, String> info) {
		for (String key : info.keySet()) {
			while (true) {
				// Present the previous try's value if there was one
				String prompt = key;
				String oldValue = info.get(key);
				if (oldValue != null) {
					prompt += " [" + oldValue + "]";
				}
				prompt += ": ";

				// Read the new value
				String line;
				try {
					line = Console.readLine(prompt, REGISTER_PROMPT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "Interrupted while prompting for register", e);
					continue;
				} catch (TimeoutException e) {
					return false;
				}

				if (line == null) {
					Console.printLine();
					continue;
				}

				line = line.trim();
				if (line.length() == 0) {
					continue;
				}

				if ("cancel".equalsIgnoreCase(line)) {
					return false;
				}

				info.put(key, line);
			}
		}

		return true;
	}

	private void printHelp() {
		Console.printLine();
		Console.printLine("help       prints this text");
		Console.printLine("register   begins the registration process");
		Console.printLine();
	}
}
