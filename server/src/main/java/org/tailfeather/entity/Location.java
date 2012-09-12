package org.tailfeather.entity;

import java.net.URI;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.IdHelper;
import org.tailfeather.resource.LocationResource;

import com.sun.jersey.server.linking.Binding;
import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;

@XmlRootElement(name = "location")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "locations")
public class Location {
	public static final String ID_COOKIE_NAME = "Tail-Feather-Location-Id";

	@Id
	@Column(name = "id")
	@XmlElement(name = "id")
	private String id;

	@XmlElement(name = "selfUri")
	@Ref(style = Style.ABSOLUTE, resource = LocationResource.class, method = "get", bindings = { @Binding(name = "id", value = "${instance.id}") })
	private URI selfUri;

	@NotNull(message = "A location name is required")
	@Size(min = 3, max = 80, message = "The location name must be 3-80 characters long")
	@Column(name = "name", nullable = false)
	@XmlElement(name = "name")
	private String name;

	public Location() {
		this.id = IdHelper.newShortId();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public URI getSelfUri() {
		return selfUri;
	}

	public void setSelfUri(URI selfUri) {
		this.selfUri = selfUri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
