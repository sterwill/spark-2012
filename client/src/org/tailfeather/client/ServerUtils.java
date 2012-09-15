package org.tailfeather.client;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.tailfeather.entity.Checkin;
import org.tailfeather.entity.Code;
import org.tailfeather.entity.User;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class ServerUtils {
	private static final Logger LOGGER = Logger.getLogger(ServerUtils.class.getName());
	private static Client client;

	public static User postUser(String resourceUri, User user) throws TailfeatherServerException {
		WebResource userResource;
		ClientResponse response;

		// hack
		user.setEmail(Long.toString(System.currentTimeMillis()) + "@example.com");

		try {
			userResource = getClient().resource(resourceUri);
			response = userResource.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, user);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Web service error", e);
			throw new TailfeatherServerException(e.getMessage());
		}
		if (response.getStatus() != HttpStatus.SC_CREATED) {
			throw new TailfeatherServerException(response.getEntity(String.class));
		}

		try {
			URI location = response.getLocation();
			response.close();

			userResource = getClient().resource(location);
			user = userResource.accept(MediaType.APPLICATION_JSON_TYPE).get(User.class);
			return user;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Web service error", e);
			throw new TailfeatherServerException(e.getMessage());
		}
	}

	public static void postSecretCode(String resourceUri, Code code) throws TailfeatherServerException {
		ClientResponse response;
		try {
			WebResource codeResource = getClient().resource(resourceUri);
			response = codeResource.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, code);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Web service error", e);
			throw new TailfeatherServerException(e.getMessage());
		}
		if (response.getStatus() != HttpStatus.SC_CREATED) {
			throw new TailfeatherServerException(response.getEntity(String.class));
		}
		response.close();
	}

	public static void postCheckin(String resourceUri, Checkin checkin) throws TailfeatherServerException {
		ClientResponse response;
		try {
			WebResource checkinResource = getClient().resource(resourceUri);
			response = checkinResource.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, checkin);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Web service error", e);
			throw new TailfeatherServerException(e.getMessage());
		}

		if (response.getStatus() != HttpStatus.SC_CREATED) {
			throw new TailfeatherServerException(response.getEntity(String.class));
		}
		response.close();
	}

	public static User getUser(User user) throws TailfeatherServerException {
		return getUser(user.getSelfUri().toString());
	}

	public static User getUser(String resourceUri) throws TailfeatherServerException {
		try {
			WebResource userResource = getClient().resource(resourceUri);
			return userResource.get(User.class);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Web service error", e);
			throw new TailfeatherServerException(e.getMessage());
		}
	}

	private static Client getClient() {
		synchronized (ServerUtils.class) {
			if (client == null) {
				ClientConfig config = new DefaultClientConfig();
				Client c = Client.create(config);
				c.setFollowRedirects(true);
				c.setConnectTimeout(5000);
				c.setReadTimeout(10000);
				c.addFilter(new HTTPBasicAuthFilter("admin", "superfoonly"));
				client = c;
			}
			return client;
		}
	}
}
