package org.tailfeather.acorn.model;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.acorn.Console;
import org.tailfeather.acorn.FileUtils;

@XmlRootElement(name = "acorn")
public class Acorn {
	private static final Logger LOGGER = Logger.getLogger(Acorn.class.getName());

	@XmlAttribute(name = "motd")
	private String motd;

	@XmlElement(name = "prompt", required = true)
	private String prompt;

	@XmlElement(name = "timeoutSeconds")
	private int timeoutSeconds = 30;

	@XmlElement(name = "command")
	private List<Command> commands;

	public void run() {
		main: while (true) {
			Console.clear();
			Console.flush();

			if (motd != null) {
				Console.print(FileUtils.getContents(motd));
				Console.flush();
			}

			while (true) {
				final String input;
				try {
					input = Console.readLine(FileUtils.getContents(prompt), timeoutSeconds, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "Interrupted while prompting", e);
					continue;
				} catch (TimeoutException e) {
					break main;
				}

				if (input == null) {
					Console.printLine();
					continue;
				}

				final String[] tokens = parseInput(input);
				if (tokens.length == 0) {
					continue;
				}

				final String commandName = tokens[0];
				commandTest: for (Command c : commands) {
					for (String name : c.getNames()) {
						if (commandName.equals(name)) {
							break commandTest;
						}
					}
				}

				printCommandError(MessageFormat.format("Unrecognized command ''{0}'', please try again", tokens[0]));
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

	private void printCommandError(String error) {
		Console.printLine();
		Console.printRedLine(error);
		Console.printLine();
	}
}
