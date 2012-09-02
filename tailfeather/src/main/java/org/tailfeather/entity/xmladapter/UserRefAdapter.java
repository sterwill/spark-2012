package org.tailfeather.entity.xmladapter;

import java.net.URI;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.tailfeather.entity.User;

public class UserRefAdapter extends XmlAdapter<URI, User> {

	@Override
	public User unmarshal(URI v) throws Exception {
		if (v == null) {
			return null;
		}
		return JAXB.unmarshal(v, User.class);
	}

	@Override
	public URI marshal(User v) throws Exception {
		if (v == null) {
			return null;
		}
		return v.getUri();
	}
}
