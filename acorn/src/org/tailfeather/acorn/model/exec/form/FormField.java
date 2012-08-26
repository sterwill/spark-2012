package org.tailfeather.acorn.model.exec.form;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.tailfeather.acorn.Console;

public abstract class FormField {
	private static final Logger LOGGER = Logger.getLogger(FormField.class.getName());

	@XmlAttribute(name = "name", required = true)
	private String name;

	@XmlAttribute(name = "prompt", required = true)
	private String prompt;

	@XmlTransient
	private String value;

	public String getName() {
		return name;
	}

	public String getPrompt() {
		return prompt;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public abstract boolean isValid();

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof FormField == false) {
			return false;
		}
		return ((FormField) obj).getName().equals(getName());
	}

	public String prompt(int promptTimeoutSeconds) {
		while (true) {
			String line;
			try {
				line = Console.readLine(prompt, promptTimeoutSeconds, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				LOGGER.log(Level.WARNING, "Interrupted while prompting for info", e);
				continue;
			} catch (TimeoutException e) {
				return null;
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
				return null;
			}

			return line;
		}
	}
}
