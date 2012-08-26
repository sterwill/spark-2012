package org.tailfeather.acorn.model.exec;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.tailfeather.acorn.Console;
import org.tailfeather.acorn.FileUtils;
import org.tailfeather.acorn.model.Command;
import org.tailfeather.acorn.model.exec.form.Email;
import org.tailfeather.acorn.model.exec.form.FormField;
import org.tailfeather.acorn.model.exec.form.Text;

@XmlRootElement(name = "form")
@XmlAccessorType(XmlAccessType.FIELD)
public class Form extends Executable {
	private static final Logger LOGGER = Logger.getLogger(Form.class.getName());

	@XmlAttribute(name = "post", required = true)
	private String post;

	@XmlAttribute(name = "instructions", required = true)
	private String instructions;

	@XmlElements({ @XmlElement(name = "text", type = Text.class), @XmlElement(name = "email", type = Email.class) })
	private List<FormField> fields = new ArrayList<FormField>();

	@XmlTransient
	private boolean valid;

	public boolean isValid() {
		return valid;
	}

	@Override
	public void execute(Command command) {
		valid = false;
		Console.print(FileUtils.getContents(instructions));
		Console.flush();

		Map<FormField, String> info = new HashMap<FormField, String>();

		while (true) {
			if (!gatherInfo(info, command)) {
				Console.printRedLine("Your registration information was NOT saved");
				return;
			}

			Console.printLine();
			Console.printLine("Please confirm the following information is correct:");
			for (FormField f : fields) {
				Console.printLine(MessageFormat.format("{0}''{1}''", f.getPrompt(), info.get(f)));
			}
			Console.printLine();

			final String input;
			try {
				input = Console.readLine("Is the information correct? (y/n/cancel) ", command.getAcorn()
						.getPromptTimeoutSeconds(), TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				LOGGER.log(Level.WARNING, "Interrupted while prompting", e);
				continue;
			} catch (TimeoutException e) {
				Console.printLine();
				Console.printRedLine("Time out after " + command.getAcorn().getPromptTimeoutSeconds() + " seconds");
				return;
			}

			if (input != null && "cancel".equalsIgnoreCase(input.trim())) {
				Console.printRedLine("Your registration information was NOT saved");
				return;
			}

			if (input != null && "y".equalsIgnoreCase(input.trim())) {
				valid = true;
				return;
			}

			// Answer was "n" or some nonsense
			Console.printLine();
		}
	}

	private boolean gatherInfo(Map<FormField, String> info, Command command) {
		info.clear();
		for (FormField field : fields) {
			info.put(field, null);
		}

		for (FormField field : info.keySet()) {
			String line = field.prompt(command.getAcorn().getPromptTimeoutSeconds());
			if (line != null) {
				info.put(field, line);
			} else {
				return false;
			}
		}

		return true;
	}
}
