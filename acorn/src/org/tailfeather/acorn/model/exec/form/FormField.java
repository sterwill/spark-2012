package org.tailfeather.acorn.model.exec.form;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

public abstract class FormField {
	@XmlAttribute(name = "name", required = true)
	private String name;

	@XmlAttribute(name = "prompt", required = true)
	private String prompt;

	@XmlTransient
	private String value;

	public String getName() {
		return name;
	}

	public String getPrompt() {
		return prompt;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public abstract boolean isValid();

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof FormField == false) {
			return false;
		}
		return ((FormField) obj).getName().equals(getName());
	}
}
