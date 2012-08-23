package org.tailfeather.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tailfeather.entity.User;
import org.tailfeather.exceptions.UserNotFoundException;
import org.tailfeather.service.UserService;
import org.tailfeather.validator.UserValidator;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserValidator userValidator;

	// RESTful Methods

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		Iterable<User> userList = userService.findAll();
		model.addAttribute("userList", userList);
		return "/user/list.jsp";
	}

	// @RequestMapping(method = RequestMethod.POST)
	// public String create(@Valid form, BindingResult result) {
	// if (result.hasErrors()) {
	// return "/user/";
	// }
	// return "redirect:/user/{id}";
	// }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String read(@PathVariable Long id, Model model) {
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return "/user/edit.jsp";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@PathVariable Long id, Model model) {
		User user = userService.findById(id);
		user.update(user);
		return "redirect:/user/{id}";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Long id, HttpServletResponse response) {
		try {
			userService.delete(id);
			response.setStatus(HttpStatus.OK.value());
		} catch (UserNotFoundException e) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}
	}

	// Forms and actions

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("user", new User());
		return "/user/form.jsp";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createSubmit(@ModelAttribute("user") User user,
			BindingResult result) {
		userValidator.validate(user, result);
		if (result.hasErrors()) {
			return "/user/form.jsp";
		}

		userService.create(user);
		return "redirect:/user";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editForm(@PathVariable Long id, Model model) {
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return "/user/form.jsp";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public String editSubmit(@ModelAttribute("user") User user,
			BindingResult result) {
		userValidator.validate(user, result);
		if (result.hasErrors()) {
			return "/user/form.jsp";
		}

		userService.update(user);
		return "redirect:/user";
	}
}