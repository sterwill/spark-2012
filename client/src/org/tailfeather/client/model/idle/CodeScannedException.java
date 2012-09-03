package org.tailfeather.client.model.idle;

public class CodeScannedException extends PromptIdleHandlerException {
	private static final long serialVersionUID = -4503164128880266003L;
	private final String code;

	public CodeScannedException(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
