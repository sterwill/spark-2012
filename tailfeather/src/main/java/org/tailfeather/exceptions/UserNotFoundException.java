package org.tailfeather.exceptions;

import java.text.MessageFormat;

public class UserNotFoundException extends Exception {
	public UserNotFoundException(String idOrUsername) {
		super(MessageFormat.format("User {0} could not be found", idOrUsername));
	}
}
