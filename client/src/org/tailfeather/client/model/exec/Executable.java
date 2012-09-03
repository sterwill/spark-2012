package org.tailfeather.client.model.exec;

import org.tailfeather.client.model.Command;

public abstract class Executable {
	public abstract void execute(Command command);
}
