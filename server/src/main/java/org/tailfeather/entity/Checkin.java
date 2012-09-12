package org.tailfeather.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.tailfeather.IdHelper;
import org.tailfeather.entity.xmladapter.DateAdapter;
import org.tailfeather.entity.xmladapter.LocationAdapter;
import org.tailfeather.entity.xmladapter.UserAdapter;

@XmlRootElement(name = "checkin")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "checkins")
public class Checkin {
	@Id
	@Column(name = "id")
	@XmlElement(name = "id")
	private String id;

	@NotNull
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@XmlElement(name = "user")
	@XmlJavaTypeAdapter(UserAdapter.class)
	private User user;

	@NotNull
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "location_id", referencedColumnName = "id")
	@XmlElement(name = "location")
	@XmlJavaTypeAdapter(LocationAdapter.class)
	private Location location;

	@NotNull
	@XmlElement(name = "time")
	@XmlJavaTypeAdapter(value = DateAdapter.class)
	private Date time;

	public Checkin() {
		this.id = IdHelper.newLongId();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
