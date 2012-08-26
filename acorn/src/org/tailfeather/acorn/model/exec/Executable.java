package org.tailfeather.acorn.model.exec;

import org.tailfeather.acorn.model.Command;

public interface Executable {
	void exececute(Command context);
}
