package org.tailfeather.client;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.tailfeather.entity.Checkin;
import org.tailfeather.entity.Code;
import org.tailfeather.entity.Location;
import org.tailfeather.entity.User;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

	private JAXBContext context;
	private Class[] types = { User.class, Location.class, Code.class, Checkin.class };

	public JAXBContextResolver() throws Exception {
		this.context = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
	}

	public JAXBContext getContext(Class<?> objectType) {
		for (Class type : types) {
			if (type == objectType) {
				return context;
			}
		}
		return null;
	}
}