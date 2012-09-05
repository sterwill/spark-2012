package org.tailfeather.client.model.exec;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
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

import org.tailfeather.client.Console;
import org.tailfeather.client.FileUtils;
import org.tailfeather.client.ServerUtils;
import org.tailfeather.client.TailfeatherServerException;
import org.tailfeather.client.model.Command;
import org.tailfeather.client.model.exec.form.Email;
import org.tailfeather.client.model.exec.form.FormField;
import org.tailfeather.client.model.exec.form.Text;
import org.tailfeather.entity.User;

@XmlRootElement(name = "register")
@XmlAccessorType(XmlAccessType.FIELD)
public class Register extends Executable {
	private static final Logger LOGGER = Logger.getLogger(Register.class.getName());

	@XmlAttribute(name = "post", required = true)
	private String postUri;

	@XmlAttribute(name = "instructions", required = true)
	private String instructions;

	@XmlAttribute(name = "success", required = true)
	private String success;

	@XmlElements({ @XmlElement(name = "text", type = Text.class), @XmlElement(name = "email", type = Email.class) })
	private List<FormField> fields = new ArrayList<FormField>();

	@Override
	public void execute(Command command) {
		Console.print(FileUtils.getContents(instructions));
		Console.flush();

		while (true) {
			if (!gatherInfo(command)) {
				Console.printRedLine("Your information was NOT saved.");
				return;
			}

			Console.printLine();
			Console.printLine("Please confirm the following information is correct:");
			Console.printLine();
			for (FormField f : fields) {
				Console.printLine(MessageFormat.format("{0}''{1}''", f.getPrompt(), f.getValue()));
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

			if (input != null && !"y".equalsIgnoreCase(input.trim())) {
				// Answer was "n" or some nonsense
				Console.printLine();
				continue;
			}

			// Submit it
			try {
				User user = submit();
				Console.printLine(MessageFormat.format(FileUtils.getContents(success), user.getId(), user.getFullName()));
				printBadge(user);
				break;
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Error submitting", e);
				Console.printRedLine("There was an error saving your information, please try again:");
				Console.printLine();
				Console.printRedLine(MessageFormat.format("{0}", e.getMessage()));
				Console.printLine();
				continue;
			}
		}
	}

	private boolean gatherInfo(Command command) {
		for (FormField field : fields) {
			String value = field.prompt(command.getAcorn().getPromptTimeoutSeconds());
			if (value != null) {
				field.setValue(value);
			} else {
				field.setValue(null);
				return false;
			}
		}

		return true;
	}

	private User submit() throws TailfeatherServerException {
		User user = new User();
		for (FormField field : fields) {
			switch (field.getName()) {
			case "name":
				user.setFullName(field.getValue());
				break;
			case "email":
				user.setEmail(field.getValue());
				break;
			}
		}
		return ServerUtils.postUser(postUri, user);
	}

	private void printBadge(User user) {
		Console.printRedLine("TODO print a badge for http://localhost:8080/web/checkin/" + user.getId());
	}
}
