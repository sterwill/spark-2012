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
import org.tailfeather.dao.LocationDao;
import org.tailfeather.entity.Location;
import org.tailfeather.exceptions.LocationNotFoundException;

@Component
@Path("/location")
public class LocationResource {
	@Autowired
	private LocationDao locationDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public List<Location> list() {
		return new ArrayList<Location>(locationDao.findAll());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/{id}")
	public Location get(@PathParam("id") String id) {
		return locationDao.findById(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	public Response create(@Valid Location location, @Context UriInfo uriInfo) {
		Location created = locationDao.create(location);
		URI uri = uriInfo.getAbsolutePathBuilder().path(Location.class).path(created.getId()).build();
		return Response.status(Status.CREATED).location(uri).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Path("/{id}")
	public Response update(@Valid Location location, @PathParam("id") String id, @Context UriInfo uriInfo) {
		location.setId(id);
		try {
			locationDao.update(location);
		} catch (LocationNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).build();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id, @Context UriInfo uriInfo) {
		try {
			locationDao.delete(id);
		} catch (LocationNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).build();
	}
}