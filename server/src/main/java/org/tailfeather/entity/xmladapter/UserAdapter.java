package org.tailfeather.entity.xmladapter;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.tailfeather.entity.User;

public class UserAdapter extends XmlAdapter<String, User> {

	@Override
	public User unmarshal(String v) throws Exception {
		if (v == null) {
			return null;
		}
		return JAXB.unmarshal(v, User.class);
	}

	@Override
	public String marshal(User v) throws Exception {
		if (v == null) {
			return null;
		}
		return v.getSelfUri().toString();
	}
}
