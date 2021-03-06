package org.tailfeather.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.tailfeather.dao.CheckinDao;
import org.tailfeather.dao.CodeDao;
import org.tailfeather.dao.UserDao;
import org.tailfeather.entity.Checkin;
import org.tailfeather.entity.User;
import org.tailfeather.exceptions.UserNotFoundException;

@Component
@Path("user")
public class UserResource {
	@Autowired
	private UserDao userDao;

	@Autowired
	private CodeDao codeDao;

	@Autowired
	private CheckinDao checkinDao;

	@Autowired
	private SimpleRequestValidator validator;

	@GET
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public List<User> list() {
		return new ArrayList<User>(userDao.findAll());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("{id}/messages")
	public List<String> messages() {
		List<String> ret = new ArrayList<String>();
		ret.add("foo");
		ret.add("bar");
		return ret;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("{id}")
	public User get(@PathParam("id") String id, @Context UriInfo uriInfo) {
		User user = userDao.findById(id);
		if (user == null) {
			return null;
		}
		user.setBadgeUri(uriInfo.getAbsolutePathBuilder().replacePath("/checkin").path(user.getId()).build());

		for (Checkin c : checkinDao.findByUser(user.getId())) {
			// cheating to reduce a fetch from the client
			c.setLocationName(c.getLocation().getName());
			c.setLocationId(c.getLocation().getId());
			user.getCheckins().add(c);
		}
		return user;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	public Response create(User user, @Context UriInfo uriInfo) {
		Response error = validator.validate(user);
		if (error != null) {
			return error;
		}

		if (userDao.findByEmail(user.getEmail()) != null) {
			return Response.status(Status.BAD_REQUEST).entity("A user with that e-mail address already exists").build();
		}

		User created = userDao.create(user);
		user.setBadgeUri(uriInfo.getAbsolutePathBuilder().replacePath("/checkin").path(created.getId()).build());

		URI uri = uriInfo.getAbsolutePathBuilder().path(UserResource.class, "get").build(created.getId());
		return Response.status(Status.CREATED).location(uri).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Path("{id}")
	public Response update(User user, @PathParam("id") String id, @Context UriInfo uriInfo) {
		Response error = validator.validate(user);
		if (error != null) {
			return error;
		}

		user.setId(id);
		try {
			userDao.update(user);
		} catch (UserNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).build();
	}

	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") String id, @Context UriInfo uriInfo) {
		try {
			userDao.delete(id);
		} catch (UserNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).build();
	}
}