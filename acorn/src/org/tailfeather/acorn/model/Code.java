package org.tailfeather.acorn.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "code")
@XmlAccessorType(XmlAccessType.FIELD)
public class Code {
	@XmlAttribute(name = "pattern", required = true)
	private String pattern;

	@XmlAttribute(name = "sound")
	private String sound;

	public String getPattern() {
		return pattern;
	}

	public Matcher getMatcher(String value) {
		if (pattern == null) {
			throw new RuntimeException("Must supply a pattern to a code element");
		}
		return Pattern.compile(pattern).matcher(value);
	}

	public String getSound() {
		return sound;
	}
}
