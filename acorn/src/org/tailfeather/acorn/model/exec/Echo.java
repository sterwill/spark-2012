package org.tailfeather.acorn.model.exec;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.acorn.model.Command;

@XmlRootElement(name = "echo")
@XmlAccessorType(XmlAccessType.FIELD)
public class Echo extends Executable {

	@XmlElement(name = "level")
	private String level;

	@XmlElement(name = "file")
	private String file;

	@Override
	public void exececute(Command context) {
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
