package org.tailfeather.client.model.exec.form;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "text")
@XmlAccessorType(XmlAccessType.FIELD)
public class Text extends FormField {
	@Override
	public boolean isValid() {
		return getValue() != null && getValue().trim().length() > 0;
	}
}
