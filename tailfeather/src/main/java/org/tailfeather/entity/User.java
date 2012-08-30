package org.tailfeather.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.util.Assert;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "users")
public class User {
	@Id
	@Column(name = "id")
	@XmlAttribute(name = "id")
	private String id;

	@NotNull
	@Size(min = 7, max = 128)
	@Pattern(regexp = ".+@.+\\.[a-z]+", message = "email.badformat")
	@Column(name = "email", nullable = false, unique = true)
	@XmlAttribute(name = "email")
	private String email;

	@NotNull
	@Size(min = 3, max = 80)
	@Column(name = "full_name", nullable = false)
	@XmlAttribute(name = "fullName")
	private String fullName;

	public User() {
		this.id = UUID.randomUUID().toString().substring(0, 7);
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
