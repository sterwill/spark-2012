package org.tailfeather.client.model;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

import org.tailfeather.client.CheckinComparator;
import org.tailfeather.client.CodeScannerRunnable;
import org.tailfeather.client.Console;
import org.tailfeather.client.FileUtils;
import org.tailfeather.client.ServerUtils;
import org.tailfeather.client.TailfeatherServerException;
import org.tailfeather.client.model.idle.CheckForScannedCodeIdleHandler;
import org.tailfeather.client.model.idle.CodeScannedException;
import org.tailfeather.client.model.idle.PromptIdleHandlerException;
import org.tailfeather.entity.Checkin;
import org.tailfeather.entity.User;

@XmlRootElement(name = "acorn")
@XmlAccessorType(XmlAccessType.FIELD)
public class Acorn {
	private static final Logger LOGGER = Logger.getLogger(Acorn.class.getName());
	private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

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
	private List<Command> commands = new ArrayList<Command>();

	@XmlElement(name = "fakeCheckin")
	private List<FakeCheckin> fakeCheckins = new ArrayList<FakeCheckin>();

	@XmlElement(name = "scan")
	private Scan scan;

	@XmlAttribute(name = "phaseThreeTriggerLocationId")
	private String phaseThreeTriggerLocationId;

	@XmlAttribute(name = "phaseThreeMessage")
	private String phaseThreeMessage;

	@XmlAttribute(name = "phaseTwoMessage")
	private String phaseTwoMessage;

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
			activeUser = null;
			Console.clear();
			Console.flush();

			if (motd != null) {
				Console.print(FileUtils.getContents(motd));
				Console.flush();
			}

			// First prompt has no timeout
			int promptTimeout = Integer.MAX_VALUE;
			while (true) {
				final String promptString = makePrompt(activeUser);
				final String input;
				try {
					scannerRunnable.clear();
					if (activeUser != null) {
						input = Console.readLine(promptString, promptTimeout, TimeUnit.SECONDS);
					} else {
						input = Console.readLine(promptString, promptTimeout, TimeUnit.SECONDS, scannerIdleHandler);
					}
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
							User user = scan.handleScan(((CodeScannedException) e).getCode());
							if (user != null) {
								activeUser = user;
								printStatus();
								// Back to prompt (with user info this time)
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
						if (commandName.equalsIgnoreCase(name)) {
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

				if (!handled && activeUser != null) {
					checkinTest: for (FakeCheckin c : fakeCheckins) {
						if (commandName.equalsIgnoreCase(c.getValue())) {
							try {
								c.report(activeUser, this);
							} catch (TailfeatherServerException e) {
								LOGGER.log(Level.SEVERE, "Error sending location code", e);
								Console.printRedLine("There was an error sending this code to the server, please try again:");
								Console.printLine();
								Console.printRedLine(MessageFormat.format("{0}", e.getMessage()));
								Console.printLine();
							}

							handled = true;
							break checkinTest;
						}
					}
				}

				if (!handled) {
					printCommandError(MessageFormat.format("Unrecognized command ''{0}'', please try again", tokens[0]));
				}
			}
		}
	}

	private String makePrompt(User user) {
		if (user == null) {
			return FileUtils.getContents(prompt);
		}

		return MessageFormat.format(FileUtils.getContents(userPrompt), user.getFullName(), user.getId());
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

	public void printStatus() {
		if (activeUser == null) {
			return;
		}
		User user = ServerUtils.getUser(activeUser.getSelfUri().toString());
		List<Checkin> checkins = user.getCheckins();

		if (checkins == null || checkins.size() == 0) {
			Console.printLine();
			Console.printLine("  You have not checked in at any other locations.");
			Console.printLine("  Show your badge to a Tail Feather agent to check in.");
			Console.printLine();
		} else {

			if (checkins.size() == 1) {
				Console.printLine();
				Console.printLine("  You have checked in at 1 location:");
			} else {
				Console.printLine();
				Console.printLine(MessageFormat.format("  You have checked in at {0} locations:", checkins.size()));
			}
			Console.printLine();

			Collections.sort(checkins, new CheckinComparator());

			for (Checkin c : checkins) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(c.getTime());

				Console.printLine(String.format("  %20s @ %s", c.getLocationName(), DATE_FORMAT.format(cal.getTime())));
			}
			Console.printLine();

			// Detect phase 2
			boolean phaseTwo = checkins.size() > 5;

			// Detect phase 3
			boolean phaseThree = false;
			for (Checkin c : checkins) {
				if (phaseThreeTriggerLocationId.equals(c.getId())) {
					phaseThree = true;
					break;
				}
			}

			if (phaseThree) {
				Console.printRed(FileUtils.getContents(phaseThreeMessage));
			} else if (phaseTwo) {
				Console.printRed(FileUtils.getContents(phaseTwoMessage));
			}
		}
	}

	public boolean hasActiveUser() {
		return activeUser != null;
	}
}
