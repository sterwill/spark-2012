package org.tailfeather.exceptions;

import java.text.MessageFormat;

public class LocationNotFoundException extends Exception {
	private static final long serialVersionUID = 8459857001002644248L;

	public LocationNotFoundException(String id) {
		super(MessageFormat.format("Location {0} could not be found", id));
	}
}
