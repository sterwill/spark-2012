package org.tailfeather.exceptions;

import java.text.MessageFormat;

public class UserNotFoundException extends Exception {
	private static final long serialVersionUID = 4221460662759353606L;

	public UserNotFoundException(String idOrUsername) {
		super(MessageFormat.format("User {0} could not be found", idOrUsername));
	}
}
