package org.tailfeather.client.model;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.client.Console;
import org.tailfeather.client.FileUtils;
import org.tailfeather.client.SoundUtils;

@XmlRootElement(name = "scan")
@XmlAccessorType(XmlAccessType.FIELD)
public class Scan {
	private static final Logger LOGGER = Logger.getLogger(Scan.class.getName());

	@XmlAttribute(name = "pattern", required = true)
	private String pattern;

	@XmlAttribute(name = "sound")
	private String sound;

	@XmlAttribute(name = "unknownSound")
	private String unknownSound;

	@XmlAttribute(name = "unknownFile")
	private String unknownFile;

	public void handleScan(Acorn acorn, String value) {
		if (value != null) {
			Matcher matcher = Pattern.compile(pattern).matcher(value);

			if (matcher.matches()) {
				SoundUtils.playSound(sound);
				handleMatch(value, matcher, acorn);
				return;
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
	}

	private void handleMatch(String value, Matcher matcher, Acorn acorn) {
		String userId = matcher.group(1);
		acorn.setActiveUserId(userId);
	}
}
