package org.tailfeather.acorn.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.acorn.Console;

@XmlRootElement(name = "scan")
@XmlAccessorType(XmlAccessType.FIELD)
public class Scan {
	private static final Logger LOGGER = Logger.getLogger(Scan.class.getName());

	@XmlAttribute(name = "unknownSound")
	private String unknownSound;

	@XmlAttribute(name = "unknownMessage")
	private String unknownMessage;

	@XmlElement(name = "code")
	private List<Code> codes = new ArrayList<Code>();

	public void handleScan(Acorn acorn, String value) {
		if (value != null) {
			boolean handled = false;
			for (Code code : codes) {
				Matcher matcher = code.getMatcher(value);
				if (matcher.matches()) {
					playSound(code.getSound());
					handled = true;
					break;
				}
			}

			if (!handled) {
				playSound(unknownSound);
				if (unknownMessage != null) {
					Console.printLine();
					Console.printLine();
					Console.printRedLine(unknownMessage);
					Console.printLine();
					Console.printLine();
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	private void playSound(String file) {
		if (file != null) {
			try {
				Clip clip = AudioSystem.getClip();
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(file));
				clip.open(inputStream);
				clip.start();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Error ringing the bell", e);
			}
		}
	}
}
