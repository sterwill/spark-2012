package org.tailfeather.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.tailfeather.dao.UserDao;
import org.tailfeather.service.UserService;

public class UserController implements Controller {
	@Autowired
	private UserService userService;

	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		UserDao user = new UserDao();
		user.setEmail("sterwill@tinfig.com");
		user.setFullName("Shaw Terwilliger");
		userService.create(user);

		return new ModelAndView("user.jsp");
	}
}
