package org.tailfeather.client.model;

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
import javax.xml.bind.annotation.XmlTransient;

import org.tailfeather.client.CodeScannerRunnable;
import org.tailfeather.client.Console;
import org.tailfeather.client.FileUtils;
import org.tailfeather.client.model.idle.CheckForScannedCodeIdleHandler;
import org.tailfeather.client.model.idle.CodeScannedException;
import org.tailfeather.client.model.idle.PromptIdleHandlerException;
import org.tailfeather.entity.User;

@XmlRootElement(name = "acorn")
@XmlAccessorType(XmlAccessType.FIELD)
public class Acorn {
	private static final Logger LOGGER = Logger.getLogger(Acorn.class.getName());

	@XmlAttribute(name = "motd")
	private String motd;

	@XmlAttribute(name = "prompt", required = true)
	private String prompt;

	@XmlAttribute(name = "userPrompt", required = true)
	private String userPrompt;

	@XmlAttribute(name = "cps")
	private int cps = 300;

	@XmlAttribute(name = "camera")
	private int camera = 0;

	@XmlAttribute(name = "promptTimeoutSeconds")
	private int promptTimeoutSeconds = 30;

	@XmlElement(name = "command")
	private List<Command> commands;

	@XmlElement(name = "scan")
	private Scan scan;

	@XmlTransient
	private CodeScannerRunnable scannerRunnable;

	@XmlTransient
	private Thread scannerThread;

	@XmlTransient
	protected String code;

	@XmlTransient
	private User activeUser;

	public int getPromptTimeoutSeconds() {
		return promptTimeoutSeconds;
	}

	public void run() {
		configureConsole();
		scannerRunnable = new CodeScannerRunnable(camera);
		scannerThread = new Thread(scannerRunnable);
		scannerThread.setDaemon(true);
		scannerThread.start();

		CheckForScannedCodeIdleHandler scannerIdleHandler = new CheckForScannedCodeIdleHandler(scannerRunnable);

		main: while (true) {
			setActiveUser(null);
			Console.clear();
			Console.flush();

			if (motd != null) {
				Console.print(FileUtils.getContents(motd));
				Console.flush();
			}

			// First prompt has no timeout
			int promptTimeout = Integer.MAX_VALUE;
			while (true) {
				final String promptString = makePrompt();
				final String input;
				try {
					scannerRunnable.clear();
					input = Console.readLine(promptString, promptTimeout, TimeUnit.SECONDS, scannerIdleHandler);
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
				} catch (PromptIdleHandlerException e) {
					if (e instanceof CodeScannedException) {
						if (scan != null) {
							if (scan.handleScan(this, ((CodeScannedException) e).getCode())) {
								// A known user ID was read
								Console.printLine();
								continue;
							}
						}
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

	private String makePrompt() {
		if (activeUser == null) {
			return FileUtils.getContents(prompt);
		}

		return MessageFormat.format(FileUtils.getContents(userPrompt), activeUser.getFullName(), activeUser.getId());
	}

	private void configureConsole() {
		Console.setOutRate(cps);
		Console.setErrRate(cps);
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

	public User getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(User activeUser) {
		this.activeUser = activeUser;
	}
}
