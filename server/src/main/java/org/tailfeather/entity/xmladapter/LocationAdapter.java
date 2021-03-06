package org.tailfeather.entity.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.stereotype.Component;
import org.tailfeather.dao.LocationDao;
import org.tailfeather.entity.Location;

@Component
public class LocationAdapter extends XmlAdapter<String, Location> {

	private LocationDao locationDao;

	public LocationAdapter() {
		this.locationDao = XmlAdapterInjectionHelper.autowire(LocationDao.class);
	}

	@Override
	public Location unmarshal(String v) throws Exception {
		if (v == null) {
			return null;
		}
		return locationDao.findById(v);
	}

	@Override
	public String marshal(Location v) throws Exception {
		return v.getId();
	}

}
