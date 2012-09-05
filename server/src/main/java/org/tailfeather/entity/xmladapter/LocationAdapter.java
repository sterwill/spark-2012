package org.tailfeather.entity.xmladapter;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.tailfeather.entity.Location;

public class LocationAdapter extends XmlAdapter<String, Location> {

	@Override
	public Location unmarshal(String v) throws Exception {
		if (v == null) {
			return null;
		}
		return JAXB.unmarshal(v, Location.class);
	}

	@Override
	public String marshal(Location v) throws Exception {
		if (v == null) {
			return null;
		}
		return v.getId();
	}
}
