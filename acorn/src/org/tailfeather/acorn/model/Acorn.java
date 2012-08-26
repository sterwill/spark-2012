package org.tailfeather.acorn.model;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.acorn.Console;
import org.tailfeather.acorn.FileUtils;

@XmlRootElement(name = "acorn")
@XmlAccessorType(XmlAccessType.FIELD)
public class Acorn {
	private static final Logger LOGGER = Logger.getLogger(Acorn.class.getName());

	@XmlAttribute(name = "motd")
	private String motd;

	@XmlAttribute(name = "prompt", required = true)
	private String prompt;

	@XmlAttribute(name = "cps")
	private int cps = 300;

	@XmlAttribute(name = "promptTimeoutSeconds")
	private int promptTimeoutSeconds = 30;

	@XmlElement(name = "command")
	private List<Command> commands;

	public int getPromptTimeoutSeconds() {
		return promptTimeoutSeconds;
	}

	public void run() {
		Console.setOutRate(cps);
		Console.setErrRate(cps);

		main: while (true) {
			Console.clear();
			Console.flush();

			if (motd != null) {
				Console.print(FileUtils.getContents(motd));
				Console.flush();
			}

			// First prompt has no timeout
			int promptTimeout = Integer.MAX_VALUE;
			while (true) {
				final String input;
				try {
					input = Console.readLine(FileUtils.getContents(prompt), promptTimeout, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "Interrupted while prompting", e);
					continue;
				} catch (TimeoutException e) {
					Console.printLine();
					printCommandError("Time out after " + promptTimeout + " seconds");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e2) {
						Thread.currentThread().interrupt();
					}
					continue main;
				}

				promptTimeout = promptTimeoutSeconds;

				if (input == null) {
					Console.printLine();
					continue;
				}

				final String[] tokens = parseInput(input);
				if (tokens.length == 0) {
					continue;
				}

				final String commandName = tokens[0];
				boolean handled = false;
				commandTest: for (Command c : commands) {
					for (String name : c.getNames()) {
						if (commandName.equals(name)) {
							c.setAcorn(this);
							c.execute();
							handled = true;

							if (c.isExit()) {
								continue main;
							}

							break commandTest;
						}
					}
				}

				if (!handled) {
					printCommandError(MessageFormat.format("Unrecognized command ''{0}'', please try again", tokens[0]));
				}
			}
		}
	}

	public static String[] parseInput(String line) {
		if (line == null || line.trim().length() == 0) {
			return new String[0];
		}

		line = line.trim();
		return line.split("\\s+");
	}

	public void printCommandError(String error) {
		Console.printRedLine(error);
	}
}
