package org.tailfeather.exceptions;

import java.text.MessageFormat;

public class CodeNotFoundException extends Exception {
	private static final long serialVersionUID = -2913259633079877517L;

	public CodeNotFoundException(String id) {
		super(MessageFormat.format("Code {0} could not be found", id));
	}
}
