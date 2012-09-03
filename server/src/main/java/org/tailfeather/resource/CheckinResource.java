package org.tailfeather.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
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
import org.tailfeather.entity.Checkin;
import org.tailfeather.exceptions.CheckinNotFoundException;

@Component
@Path("/checkin")
public class CheckinResource {
	@Autowired
	private CheckinDao checkinDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public List<Checkin> list() {
		return new ArrayList<Checkin>(checkinDao.findAll());
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	public Response create(@Valid Checkin checkin, @Context UriInfo uriInfo) {
		Checkin created = checkinDao.create(checkin);
		URI uri = uriInfo.getAbsolutePathBuilder().path(Checkin.class).path(created.getId()).build();
		return Response.status(Status.CREATED).location(uri).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Path("/{id}")
	public Response update(@Valid Checkin checkin, @PathParam("id") String id, @Context UriInfo uriInfo) {
		checkin.setId(id);
		try {
			checkinDao.update(checkin);
		} catch (CheckinNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).build();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id, @Context UriInfo uriInfo) {
		try {
			checkinDao.delete(id);
		} catch (CheckinNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).build();
	}
}