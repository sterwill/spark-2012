package org.tailfeather.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.IdHelper;

@XmlRootElement(name = "location")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "locations")
public class Location {
	public static final String ID_COOKIE_NAME = "Tail-Feather-Location-Id";

	@Id
	@Column(name = "id")
	@XmlAttribute(name = "id")
	private String id;

	@NotNull(message = "A location name is required")
	@Size(min = 3, max = 80, message = "The location name must be 3-80 characters long")
	@Column(name = "name", nullable = false)
	@XmlAttribute(name = "name")
	private String name;

	public Location() {
		this.id = IdHelper.newLongId();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
