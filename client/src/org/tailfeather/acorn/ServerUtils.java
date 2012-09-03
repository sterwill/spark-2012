package org.tailfeather.acorn;

import java.net.URI;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.tailfeather.entity.User;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class ServerUtils {
	private static final Logger LOGGER = Logger.getLogger(ServerUtils.class.getName());
	private static Client client;

	public static User postUser(String resourceUri, User user) {
		WebResource userResource = getClient().resource(resourceUri);
		ClientResponse response = userResource.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, user);
		URI location = response.getLocation();
		response.close();

		userResource = getClient().resource(location);
		user = userResource.accept(MediaType.APPLICATION_JSON_TYPE).get(User.class);
		return user;
	}

	public static User getUser(String resourceUri) {
		WebResource userResource = getClient().resource(resourceUri);
		return userResource.get(User.class);
	}

	private static Client getClient() {
		synchronized (ServerUtils.class) {
			if (client == null) {
				ClientConfig config = new DefaultClientConfig();
				Client c = Client.create(config);
				c.setFollowRedirects(true);
				c.setConnectTimeout(5000);
				c.setReadTimeout(10000);
				client = c;
			}
			return client;
		}
	}
}
