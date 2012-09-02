package org.tailfeather.entity.xmladapter;

import java.net.URI;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.tailfeather.entity.Checkin;

public class CheckinAdapter extends XmlAdapter<URI, Checkin> {

	@Override
	public Checkin unmarshal(URI v) throws Exception {
		if (v == null) {
			return null;
		}
		return JAXB.unmarshal(v, Checkin.class);
	}

	@Override
	public URI marshal(Checkin v) throws Exception {
		if (v == null) {
			return null;
		}
		return v.getUri();
	}
}
