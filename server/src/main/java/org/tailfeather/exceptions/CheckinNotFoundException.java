package org.tailfeather.exceptions;

import java.text.MessageFormat;

public class CheckinNotFoundException extends Exception {
	private static final long serialVersionUID = 4683997696485216344L;

	public CheckinNotFoundException(String id) {
		super(MessageFormat.format("Checkin {0} could not be found", id));
	}
}
