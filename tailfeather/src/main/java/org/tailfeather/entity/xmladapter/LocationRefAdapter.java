package org.tailfeather.entity.xmladapter;

import java.net.URI;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.tailfeather.entity.Location;

public class LocationRefAdapter extends XmlAdapter<URI, Location> {

	@Override
	public Location unmarshal(URI v) throws Exception {
		if (v == null) {
			return null;
		}
		return JAXB.unmarshal(v, Location.class);
	}

	@Override
	public URI marshal(Location v) throws Exception {
		if (v == null) {
			return null;
		}
		return v.getUri();
	}
}
