package org.tailfeather.acorn.model.idle;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultPromptIdleHandler implements PromptIdleHandler {
	private static final Logger LOGGER = Logger.getLogger(DefaultPromptIdleHandler.class.getName());

	@Override
	public void idle() throws CodeScannedException {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			LOGGER.log(Level.INFO, "interrupted in prompt idle handler", e);
		}
	}
}
