package org.tailfeather.acorn.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "scan")
@XmlAccessorType(XmlAccessType.FIELD)
public class Scan {
	private static final Logger LOGGER = Logger.getLogger(Scan.class.getName());

	@XmlAttribute(name = "bell")
	private String bell;

	@XmlElement(name = "code")
	private List<Code> codes = new ArrayList<Code>();

	public void handleScan(Acorn acorn, String value) {
		ringBell();

		if (value != null) {
			for (Code code : codes) {
				if (value.equals(code.getValue()) && code.getCommand() != null) {
					code.getCommand().setAcorn(acorn);
					code.getCommand().execute();
				}
			}
		}
	}

	private void ringBell() {
		if (bell != null) {
			try {
				Clip clip = AudioSystem.getClip();
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(bell));
				clip.open(inputStream);
				clip.start();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Error ringing the bell", e);
			}
		}
	}
}
