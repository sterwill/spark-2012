package org.tailfeather.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.util.Assert;
import org.tailfeather.IdHelper;
import org.tailfeather.resource.UserResource;

import com.sun.jersey.server.linking.Binding;
import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "users")
public class User {
	@Id
	@Column(name = "id")
	@XmlElement(name = "id")
	private String id;

	@XmlElement(name = "selfUri")
	@Ref(style = Style.ABSOLUTE, resource = UserResource.class, method = "get", bindings = { @Binding(name = "id", value = "${instance.id}") })
	private URI selfUri;

	@XmlElement(name = "checkinUri")
	private URI checkinUri;

	@NotNull(message = "An e-mail address is required")
	@Size(min = 7, max = 128, message = "The e-mail address must be 7-128 characters long")
	@Pattern(regexp = ".+@.+\\.[a-z]+", message = "The e-mail address must be in the form user@domain")
	@Column(name = "email", nullable = false, unique = true)
	@XmlElement(name = "email")
	private String email;

	@NotNull(message = "A full name is required")
	@Size(min = 3, max = 80, message = "The name must be 3-80 characters long")
	@Column(name = "full_name", nullable = false)
	@XmlElement(name = "fullName")
	private String fullName;

	@Transient
	@XmlElement(name = "checkins")
	private List<Checkin> checkins = new ArrayList<Checkin>();

	public User() {
		this.id = IdHelper.newShortId();
	}

	public User(String email, String fullName) {
		this();
		Assert.hasText(email);
		Assert.hasText(fullName);
		this.email = email;
		this.fullName = fullName;
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

	public URI getCheckinUri() {
		return checkinUri;
	}

	public void setCheckinUri(URI checkinUri) {
		this.checkinUri = checkinUri;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public User update(User user) {
		this.email = user.email;
		this.fullName = user.fullName;
		return this;
	}

	public List<Checkin> getCheckins() {
		return checkins;
	}

	public void setCheckins(List<Checkin> checkins) {
		this.checkins = checkins;
	}
}
