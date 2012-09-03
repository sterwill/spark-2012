package org.tailfeather.client.model.exec;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.client.model.Command;

@XmlRootElement(name = "exit")
@XmlAccessorType(XmlAccessType.FIELD)
public class Exit extends Executable {
	@Override
	public void execute(Command command) {
		command.setExit(true);
	}
}
