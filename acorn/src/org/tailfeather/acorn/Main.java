package org.tailfeather.acorn;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;

import org.tailfeather.acorn.model.Acorn;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	private static final int MAIN_PROMPT_TIMEOUT_SECONDS = 30;
	private static final int REGISTER_PROMPT_TIMEOUT_SECONDS = 60;

	public static void main(String[] args) throws Exception {
		LogConfig.configureRootLogger();

		try {
			System.exit(new Main().run(args));
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Uncaught exception", e);
			e.printStackTrace();
		}
	}

	public Main() {
	}

	public int run(String args[]) throws Exception {
		if (args.length != 1) {
			System.err.println("usage: " + Main.class.getName() + " acorn.xml");
			return 1;
		}

		Acorn acorn = (Acorn) JAXBContext.newInstance(Acorn.class).createUnmarshaller().unmarshal(new File(args[0]));
		if (acorn == null) {
			System.err.println("Error reading " + args[0]);
			return 2;
		}

		acorn.run();
		return 0;
	}
}
