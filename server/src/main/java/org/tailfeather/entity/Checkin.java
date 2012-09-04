package org.tailfeather.entity;

import java.net.URI;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.tailfeather.IdHelper;
import org.tailfeather.entity.xmladapter.LocationRefAdapter;
import org.tailfeather.entity.xmladapter.UserRefAdapter;

import com.sun.jersey.server.linking.Binding;
import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;

@XmlRootElement(name = "checkin")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "checkins")
public class Checkin {
	@Ref(value = "checkin/{id}", style = Style.ABSOLUTE, bindings = { @Binding(name = "id", value = "${instance.id}") })
	@XmlAttribute(name = "uri")
	@Transient
	private URI uri;

	@Id
	@Column(name = "id")
	@XmlAttribute(name = "id")
	private String id;

	@NotNull
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@XmlAttribute(name = "user")
	@XmlJavaTypeAdapter(UserRefAdapter.class)
	private User user;

	@NotNull
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "location_id", referencedColumnName = "id")
	@XmlAttribute(name = "location")
	@XmlJavaTypeAdapter(LocationRefAdapter.class)
	private Location location;

	@NotNull
	@XmlAttribute(name = "time")
	private Date time;

	public Checkin() {
		this.id = IdHelper.newLongId();
	}

	public URI getUri() {
		return uri;
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
