package org.tailfeather.client.model.exec;

import org.tailfeather.client.model.Command;

public abstract class Executable {
	public boolean enabled(Command command) {
		return true;
	}

	public abstract void execute(Command command);
}
