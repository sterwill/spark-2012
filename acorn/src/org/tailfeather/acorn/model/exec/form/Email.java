package org.tailfeather.acorn.model.exec.form;

import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "email")
@XmlAccessorType(XmlAccessType.FIELD)
public class Email extends FormField {
	@Override
	public boolean isValid() {
		if (getValue() == null) {
			return false;
		}

		String pattern = "^[!#-'\\*\\+\\-/0-9=\\?A-Z\\^_`a-z{-~]+(\\.[!#-'\\*\\+\\-/0-9=\\?A-Z\\^_`a-z{-~]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		return Pattern.compile(pattern).matcher(getValue()).matches();
	}
}
