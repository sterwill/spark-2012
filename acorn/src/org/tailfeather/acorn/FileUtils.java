package org.tailfeather.acorn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils {
	private final static Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

	public static String getContents(String path) {
		InputStream stream;
		try {
			stream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			String error = MessageFormat.format("Error: {0} not found", path);
			LOGGER.log(Level.WARNING, error);
			return error;
		}

		StringWriter writer = new StringWriter();
		try {
			int c;
			while ((c = stream.read()) != -1) {
				writer.write(c);
			}
		} catch (IOException e) {
			String error = MessageFormat.format("Error reading file {0}", path);
			LOGGER.log(Level.WARNING, error);
			return error;
		}

		return writer.toString();
	}
}
