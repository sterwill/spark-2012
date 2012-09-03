package org.tailfeather.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.client.Console;
import org.tailfeather.client.FileUtils;
import org.tailfeather.client.SoundUtils;

@XmlRootElement(name = "scan")
@XmlAccessorType(XmlAccessType.FIELD)
public class Scan {
	private static final Logger LOGGER = Logger.getLogger(Scan.class.getName());

	@XmlAttribute(name = "unknownSound")
	private String unknownSound;

	@XmlAttribute(name = "unknownFile")
	private String unknownFile;

	@XmlElement(name = "code")
	private List<Code> codes = new ArrayList<Code>();

	public void handleScan(Acorn acorn, String value) {
		if (value != null) {
			boolean handled = false;
			for (Code code : codes) {
				if (code.evaluate(value)) {
					handled = true;
					break;
				}
			}

			if (!handled) {
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
	}
}
