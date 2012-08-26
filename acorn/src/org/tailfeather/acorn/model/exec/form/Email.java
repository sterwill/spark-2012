package org.tailfeather.acorn.model.exec.form;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "email")
public class Email implements FormFields {
	@XmlAttribute(name = "name")
	private String name;

	@XmlTransient
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean isValid() {
		return false;
	}
}
