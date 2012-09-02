package org.tailfeather.exceptions;

import java.text.MessageFormat;

public class CheckinNotFoundException extends Exception {
	public CheckinNotFoundException(String id) {
		super(MessageFormat.format("Checkin {0} could not be found", id));
	}
}
