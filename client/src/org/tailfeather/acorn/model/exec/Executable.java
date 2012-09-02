package org.tailfeather.acorn.model.exec;

import org.tailfeather.acorn.model.Command;

public abstract class Executable {
	public abstract void execute(Command command);
}
