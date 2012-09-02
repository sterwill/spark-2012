package org.tailfeather.acorn.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.tailfeather.acorn.SoundUtils;

@XmlRootElement(name = "code")
@XmlAccessorType(XmlAccessType.FIELD)
public class Code {
	private static final Logger LOGGER = Logger.getLogger(Code.class.getName());

	@XmlAttribute(name = "pattern", required = true)
	private String pattern;

	@XmlAttribute(name = "sound")
	private String sound;

	@XmlAttribute(name = "postUriFormat")
	private String postUriFormat;

	public boolean evaluate(String value) {
		if (pattern == null) {
			throw new RuntimeException("Must supply a pattern to a code element");
		}
		Matcher matcher = Pattern.compile(pattern).matcher(value);

		if (matcher.matches()) {
			SoundUtils.playSound(sound);
			handleMatch(value);
			return true;
		}

		return false;
	}

	private void handleMatch(String value) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// for (FormField field : fields) {
		// params.add(new BasicNameValuePair(field.getName(),
		// field.getValue()));
		// }

		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.log(Level.WARNING, "Error preparing request entity", e);
			return;
		}

		HttpPost post = new HttpPost(postUriFormat);
		post.setEntity(entity);

		HttpResponse response;
		try {
			HttpClient client = new DefaultHttpClient();
			response = client.execute(post);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error submitting form", e);
			return;
		}

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
				|| response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED
				|| response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {
			return;
		}

		LOGGER.log(Level.WARNING, "Got non-success for " + postUriFormat + ": " + response.getStatusLine());
		return;
	}
}
