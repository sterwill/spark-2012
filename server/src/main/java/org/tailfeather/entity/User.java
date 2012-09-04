package org.tailfeather.entity;

import java.net.URI;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.util.Assert;
import org.tailfeather.IdHelper;

import com.sun.jersey.server.linking.Binding;
import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "users")
public class User {
	@Ref(value = "users/{id}", style = Style.ABSOLUTE, bindings = { @Binding(name = "id", value = "${instance.id}") })
	@XmlElement(name = "uri")
	private URI uri;

	@Id
	@Column(name = "id")
	@XmlElement(name = "id")
	private String id;

	@NotNull(message = "An e-mail address is required")
	@Size(min = 7, max = 128, message = "The e-mail address must be at least 7 characters long")
	@Pattern(regexp = ".+@.+\\.[a-z]+", message = "The e-mail address must be in the form user@domain")
	@Column(name = "email", nullable = false, unique = true)
	@XmlElement(name = "email")
	private String email;

	@NotNull(message = "A full name is required")
	@Size(min = 3, max = 80, message = "The name must be at least 3 characters long")
	@Column(name = "full_name", nullable = false)
	@XmlElement(name = "fullName")
	private String fullName;

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

	public URI getUri() {
		return uri;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}
