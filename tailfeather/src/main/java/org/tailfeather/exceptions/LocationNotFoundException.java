package org.tailfeather.exceptions;

import java.text.MessageFormat;

public class LocationNotFoundException extends Exception {
	public LocationNotFoundException(String id) {
		super(MessageFormat.format("Location {0} could not be found", id));
	}
}
