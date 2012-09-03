package org.tailfeather.entity;

import java.net.URI;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.IdHelper;
import org.tailfeather.resource.LocationResource;

import com.sun.jersey.server.linking.Ref;

@XmlRootElement(name = "location")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "locations")
public class Location {
	@Ref(resource = LocationResource.class)
	@XmlAttribute(name = "uri")
	@Transient
	private URI uri;

	@Id
	@Column(name = "id")
	@XmlAttribute(name = "id")
	private String id;

	@NotNull
	@Size(min = 3, max = 80)
	@Column(name = "name", nullable = false)
	@XmlAttribute(name = "name")
	private String name;

	public Location() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}