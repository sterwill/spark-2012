package org.tailfeather.acorn.model.exec;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.acorn.model.Command;
import org.tailfeather.acorn.model.exec.form.Email;
import org.tailfeather.acorn.model.exec.form.FormField;
import org.tailfeather.acorn.model.exec.form.Text;

@XmlRootElement(name = "form")
@XmlAccessorType(XmlAccessType.FIELD)
public class Form extends Executable {

	@XmlAttribute(name = "post")
	private String postUri;

	@XmlAttribute(name = "instructions")
	private String instructionsFile;

	@XmlElements({ @XmlElement(name = "text", type = Text.class), @XmlElement(name = "email", type = Email.class) })
	private List<FormField> fields = new ArrayList<FormField>();

	public String getPostUri() {
		return postUri;
	}

	public void setPostUri(String postUri) {
		this.postUri = postUri;
	}

	public String getInstructionsFile() {
		return instructionsFile;
	}

	public void setInstructionsFile(String instructionsFile) {
		this.instructionsFile = instructionsFile;
	}

	public List<FormField> getFields() {
		return fields;
	}

	public void setFields(List<FormField> fields) {
		this.fields = fields;
	}

	@Override
	public void exececute(Command context) {
	}
}
