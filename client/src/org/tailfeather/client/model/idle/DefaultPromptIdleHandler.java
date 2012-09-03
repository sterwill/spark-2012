package org.tailfeather.client.model.idle;

import java.util.logging.Logger;

public class DefaultPromptIdleHandler implements PromptIdleHandler {
	private static final Logger LOGGER = Logger.getLogger(DefaultPromptIdleHandler.class.getName());

	@Override
	public void idle() throws CodeScannedException, InterruptedException {
		Thread.sleep(50);
	}
}
