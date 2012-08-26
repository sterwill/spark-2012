package org.tailfeather.acorn.model.exec;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.acorn.model.Command;

@XmlRootElement(name = "echo")
public class Echo implements Executable {

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
