package org.tailfeather.client.model.exec;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.client.Console;
import org.tailfeather.client.model.Command;

@XmlRootElement(name = "printStatus")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrintStatus extends Executable {

	@Override
	public void execute(Command command) {
		if (command.getAcorn().hasActiveUser()) {
			command.getAcorn().printStatus();
		} else {
			Console.printLine();
			Console.printRedLine("You must authenticate with your badge to see your status.");
			Console.printLine();
		}
	}
}
