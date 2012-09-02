package org.tailfeather.acorn;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogConfig {
	public static void configureRootLogger() throws SecurityException, IOException {
		Logger root = LogManager.getLogManager().getLogger("");
		for (Handler h : root.getHandlers()) {
			root.removeHandler(h);
		}
		final FileHandler fileHandler = new FileHandler("/tmp/acorn.log", true);
		fileHandler.setFormatter(new SimpleFormatter());
		root.addHandler(fileHandler);
	}
}
