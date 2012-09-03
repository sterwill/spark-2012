package org.tailfeather.client.model.idle;

public class PromptIdleHandlerException extends Exception {
	private static final long serialVersionUID = -3365140919879388226L;

	public PromptIdleHandlerException() {
		super();
	}

	public PromptIdleHandlerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PromptIdleHandlerException(String message, Throwable cause) {
		super(message, cause);
	}

	public PromptIdleHandlerException(String message) {
		super(message);
	}

	public PromptIdleHandlerException(Throwable cause) {
		super(cause);
	}
}
