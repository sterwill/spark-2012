package org.tailfeather.client.model.idle;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.tailfeather.client.CodeScannerRunnable;

public class CheckForScannedCodeIdleHandler implements PromptIdleHandler {
	private static final Logger LOGGER = Logger.getLogger(CheckForScannedCodeIdleHandler.class.getName());
	private static final int LOCKOUT_SECONDS = 10;

	private final CodeScannerRunnable scanner;
	private long lastFound = -1;

	public CheckForScannedCodeIdleHandler(CodeScannerRunnable scanner) {
		this.scanner = scanner;
	}

	@Override
	public void idle() throws CodeScannedException, InterruptedException {
		final String code = scanner.getCode();
		scanner.clear();

		long now = System.currentTimeMillis();
		if (code != null && (now - lastFound) > LOCKOUT_SECONDS * 1000) {
			lastFound = now;
			LOGGER.log(Level.INFO, "Scanned [{0}]", code);
			throw new CodeScannedException(code);
		}

		Thread.sleep(50);
	}
}
