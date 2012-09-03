package org.tailfeather.acorn.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.tailfeather.acorn.model.exec.Echo;
import org.tailfeather.acorn.model.exec.Executable;
import org.tailfeather.acorn.model.exec.Exit;
import org.tailfeather.acorn.model.exec.Register;

@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class Command {
	@XmlElement(name = "name")
	private List<String> names;

	@XmlElements({ @XmlElement(name = "echo", type = Echo.class),
			@XmlElement(name = "register", type = Register.class), @XmlElement(name = "exit", type = Exit.class) })
	private List<Executable> exec = new ArrayList<Executable>();

	@XmlTransient
	private boolean exit;

	@XmlTransient
	private Acorn acorn;

	public List<String> getNames() {
		return names;
	}

	public boolean isExit() {
		return this.exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

	public void setAcorn(Acorn acorn) {
		this.acorn = acorn;
	}

	public Acorn getAcorn() {
		return acorn;
	}

	public void execute() {
		if (exec.size() == 0) {
			return;
		}

		for (Executable e : exec) {
			e.execute(this);
		}
	}
}