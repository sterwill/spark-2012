package org.tailfeather.acorn.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.acorn.model.exec.Echo;
import org.tailfeather.acorn.model.exec.Executable;
import org.tailfeather.acorn.model.exec.Exit;
import org.tailfeather.acorn.model.exec.Form;

@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class Command {
	@XmlElement(name = "name")
	private List<String> names;

	@XmlElements({ @XmlElement(name = "echo", type = Echo.class), @XmlElement(name = "register", type = Form.class),
			@XmlElement(name = "exit", type = Exit.class) })
	private List<Executable> exec = new ArrayList<Executable>();

	private boolean exit;

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public List<Executable> getExec() {
		return exec;
	}

	public void setExec(List<Executable> exec) {
		this.exec = exec;
	}

	public boolean isExit() {
		return this.exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

	public void execute() {
		if (exec.size() == 0) {
			return;
		}

		for (Executable e : exec) {
			e.exececute(this);
		}
	}
}
