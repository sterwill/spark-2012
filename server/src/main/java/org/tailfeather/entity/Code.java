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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.tailfeather.IdHelper;
import org.tailfeather.entity.xmladapter.UserAdapter;

import com.sun.jersey.server.linking.Binding;
import com.sun.jersey.server.linking.Ref;
import com.sun.jersey.server.linking.Ref.Style;

@XmlRootElement(name = "code")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "codes")
public class Code {
	@Ref(value = "code/{id}", style = Style.ABSOLUTE, bindings = { @Binding(name = "id", value = "${instance.id}") })
	@XmlAttribute(name = "uri")
	@Transient
	private URI uri;

	@Column(name = "code")
	@XmlAttribute(name = "code")
	private String code;

	@Id
	@Column(name = "id")
	@XmlAttribute(name = "id")
	private String id;

	@NotNull
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@XmlAttribute(name = "userId")
	@XmlJavaTypeAdapter(UserAdapter.class)
	private User user;

	@NotNull
	@XmlElement(name = "time")
	private Date time;

	public Code() {
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
