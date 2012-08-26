package org.tailfeather.acorn.model.exec;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.acorn.Console;
import org.tailfeather.acorn.FileUtils;
import org.tailfeather.acorn.model.Command;

@XmlRootElement(name = "echo")
@XmlAccessorType(XmlAccessType.FIELD)
public class Echo extends Executable {
	private static final String LEVEL_ERROR = "error";

	@XmlAttribute(name = "level")
	private String level;

	@XmlAttribute(name = "file", required = true)
	private String file;

	@Override
	public void execute(Command command) {
		String message = FileUtils.getContents(file);
		if (LEVEL_ERROR.equals(level)) {
			Console.printRedLine(message);
		} else {
			Console.printLine(message);
		}
	}
}
