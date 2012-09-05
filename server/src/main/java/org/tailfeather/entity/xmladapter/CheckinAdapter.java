package org.tailfeather.entity.xmladapter;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.tailfeather.entity.Checkin;

public class CheckinAdapter extends XmlAdapter<String, Checkin> {

	@Override
	public Checkin unmarshal(String v) throws Exception {
		if (v == null) {
			return null;
		}
		return JAXB.unmarshal(v, Checkin.class);
	}

	@Override
	public String marshal(Checkin v) throws Exception {
		if (v == null) {
			return null;
		}
		return v.getId();
	}
}
