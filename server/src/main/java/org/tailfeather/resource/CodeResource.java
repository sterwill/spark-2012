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
import org.tailfeather.dao.CodeDao;
import org.tailfeather.entity.Code;
import org.tailfeather.exceptions.CodeNotFoundException;

@Component
@Path("/code/{id}")
public class CodeResource {
	@Autowired
	private CodeDao codeDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public List<Code> list() {
		return new ArrayList<Code>(codeDao.findAll());
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	public Response create(@Valid Code code, @Context UriInfo uriInfo) {
		Code created = codeDao.create(code);
		URI uri = uriInfo.getAbsolutePathBuilder().path(Code.class).path(created.getId()).build();
		return Response.status(Status.CREATED).location(uri).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Path("/{id}")
	public Response update(@Valid Code code, @PathParam("id") String id, @Context UriInfo uriInfo) {
		code.setId(id);
		try {
			codeDao.update(code);
		} catch (CodeNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).build();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id, @Context UriInfo uriInfo) {
		try {
			codeDao.delete(id);
		} catch (CodeNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).build();
	}
}