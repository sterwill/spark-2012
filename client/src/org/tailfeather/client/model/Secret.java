package org.tailfeather.client.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.client.ServerUtils;
import org.tailfeather.client.TailfeatherServerException;
import org.tailfeather.entity.Code;
import org.tailfeather.entity.User;

@XmlRootElement(name = "secret")
@XmlAccessorType(XmlAccessType.FIELD)
public class Secret {
	@XmlElement(name = "value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void report(User user) throws TailfeatherServerException {
		Code code = new Code();
		code.setUser(user);
		code.setCode(value);
		code.setTime(new Date());
		ServerUtils.postSecretCode(user.getCodeUri().toString(), code);

		// ServerUtils.getMessages(user.getMessagesUri());
	}
}
