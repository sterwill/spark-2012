package org.tailfeather.acorn;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.MessageFormat;

public class Resources {
	public static String getMessage(String name) {
		InputStream stream = Resources.class.getClassLoader().getResourceAsStream(name);
		if (stream == null) {
			throw new RuntimeException(MessageFormat.format("Message resource {0} not found", name));
		}

		StringWriter writer = new StringWriter();
		try {
			int c;
			while ((c = stream.read()) != -1) {
				writer.write(c);
			}
		} catch (IOException e) {
			throw new RuntimeException(MessageFormat.format("Error reading message resource {0}", name), e);
		}

		return writer.toString();
	}
}
