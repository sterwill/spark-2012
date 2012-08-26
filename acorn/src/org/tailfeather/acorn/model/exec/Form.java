package org.tailfeather.acorn.model.exec;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
				Console.printRedLine("Your registration information was NOT saved");
				return;
			}

			Console.printLine();
			Console.printLine("Please confirm the following information is correct:");
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
			if (!submit()) {
				Console.printRedLine("There was an error saving your information, please try again");
				continue;
			} else {
				Console.printLine(FileUtils.getContents(success));
				break;
			}
		}
	}

	private boolean submit() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (FormField field : fields) {
			params.add(new BasicNameValuePair(field.getName(), field.getValue()));
		}

		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.log(Level.WARNING, "Error preparing request entity", e);
			return false;
		}

		HttpPost post = new HttpPost(postUri);
		post.setEntity(entity);

		HttpResponse response;
		try {
			HttpClient client = new DefaultHttpClient();
			response = client.execute(post);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error submitting form", e);
			return false;
		}

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
				|| response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED
				|| response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {
			return true;
		}

		LOGGER.log(Level.WARNING, "Got non-success for " + postUri + ": " + response.getStatusLine());
		return false;
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
}
