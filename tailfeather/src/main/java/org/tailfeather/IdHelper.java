package org.tailfeather;

import java.util.UUID;

public class IdHelper {

	public static String newShortId() {
		return newLongId().substring(0, 7);
	}

	public static String newLongId() {
		return UUID.randomUUID().toString();
	}
}
