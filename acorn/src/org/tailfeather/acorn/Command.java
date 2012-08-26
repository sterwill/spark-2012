package org.tailfeather.acorn;

public class Command {

	public static String[] parse(String line) {
		if (line == null || line.trim().length() == 0) {
			return new String[0];
		}

		line = line.trim();
		return line.split("\\s+");
	}
}
