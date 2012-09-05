package org.tailfeather.controller;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "index.jsp";
	}

	@RequestMapping(value = "/favicon.ico", method = RequestMethod.GET)
	public void favicon(HttpServletResponse response) {
		response.setStatus(Status.NOT_FOUND.getStatusCode());
	}
}