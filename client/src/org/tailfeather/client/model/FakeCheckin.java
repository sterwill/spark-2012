package org.tailfeather.client.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.client.Console;
import org.tailfeather.client.ServerUtils;
import org.tailfeather.client.TailfeatherServerException;
import org.tailfeather.entity.Checkin;
import org.tailfeather.entity.Location;
import org.tailfeather.entity.User;

@XmlRootElement(name = "fakeCheckin")
@XmlAccessorType(XmlAccessType.FIELD)
public class FakeCheckin {

	@XmlAttribute(name = "value")
	private String value;

	@XmlAttribute(name = "locationId")
	private String locationId;

	@XmlAttribute(name = "locationName")
	private String locationName;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public void report(User user, Acorn acorn) throws TailfeatherServerException {
		Location location = new Location();
		location.setId(locationId);

		Checkin checkin = new Checkin();
		checkin.setLocation(location);
		checkin.setUser(user);
		checkin.setTime(new Date());

		ServerUtils.postCheckin(user.getCheckinUri().toString(), checkin);

		Console.printLine();
		Console.printRed(String.format("  >>> Checked into %s", locationName));
		Console.printLine();

		acorn.printStatus();
	}
}
