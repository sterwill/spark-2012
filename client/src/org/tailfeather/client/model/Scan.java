package org.tailfeather.client.model;

import java.text.MessageFormat;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.client.Console;
import org.tailfeather.client.FileUtils;
import org.tailfeather.client.ServerUtils;
import org.tailfeather.client.SoundUtils;
import org.tailfeather.client.TailfeatherServerException;
import org.tailfeather.entity.User;

@XmlRootElement(name = "scan")
@XmlAccessorType(XmlAccessType.FIELD)
public class Scan {
	private static final Logger LOGGER = Logger.getLogger(Scan.class.getName());

	@XmlAttribute(name = "pattern", required = true)
	private String pattern;

	@XmlAttribute(name = "knownSound")
	private String knownSound;

	@XmlAttribute(name = "knownFile")
	private String knownFile;

	@XmlAttribute(name = "unknownSound")
	private String unknownSound;

	@XmlAttribute(name = "unknownFile")
	private String unknownFile;

	public User handleScan(String value) {
		if (value != null) {
			Matcher matcher = Pattern.compile(pattern).matcher(value);
			if (matcher.matches()) {
				return getMatchedUser(value, matcher);
			}

			SoundUtils.playSound(unknownSound);
			if (unknownFile != null) {
				Console.printLine();
				Console.printRedLine(FileUtils.getContents(unknownFile));
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}
			}
		}
		return null;
	}

	private User getMatchedUser(String value, Matcher matcher) {
		String checkinUri = value;
		String host = matcher.group(1);
		String userId = matcher.group(2);

		SoundUtils.playSound(knownSound);

		Console.printLine();
		Console.printLine(MessageFormat.format("Retrieving information for user {0}...", userId));

		User user;
		try {
			user = ServerUtils.getUser(String.format("http://%s/api/user/%s", host, userId));
		} catch (TailfeatherServerException e) {
			Console.printLine();
			Console.printRedLine("There was an error getting your information from the server.");
			Console.printRedLine("Please contact a Tail Feather administrator.");
			Console.printLine();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e2) {
			}

			return null;
		}

		if (user != null) {
			if (knownFile != null) {
				Console.print(MessageFormat.format(FileUtils.getContents(knownFile), user.getFullName()));
			}
			return user;
		}

		Console.printLine();
		Console.printRedLine("Your information could not be retrieved at this time.");
		Console.printLine();
		return null;
	}
}