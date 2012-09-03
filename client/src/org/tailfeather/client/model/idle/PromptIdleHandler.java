package org.tailfeather.client.model.idle;

public interface PromptIdleHandler {
	void idle() throws CodeScannedException, InterruptedException;
}
