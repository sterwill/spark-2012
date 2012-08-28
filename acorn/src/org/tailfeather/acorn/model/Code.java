package org.tailfeather.acorn.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "code")
@XmlAccessorType(XmlAccessType.FIELD)
public class Code {
	@XmlElement(name = "value")
	private String value;

	@XmlElement(name = "command")
	private Command command;

	public String getValue() {
		return value;
	}

	public Command getCommand() {
		return command;
	}
}
