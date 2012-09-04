package org.tailfeather.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tailfeather.dao.UserDao;
import org.tailfeather.entity.User;
import org.tailfeather.exceptions.UserNotFoundException;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserDao userDao;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("userList", userDao.findAll());
		return "/user/list.jsp";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("user", new User());
		return "/user/form.jsp";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createSubmit(@Valid @ModelAttribute("user") User user, BindingResult result) {
		if (result.hasErrors()) {
			return "/user/form.jsp";
		}
		userDao.create(user);
		return "redirect:/user";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editForm(@PathVariable String id, @Valid Model model) {
		User user = userDao.findById(id);
		model.addAttribute("user", user);
		return "/user/form.jsp";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public String editSubmit(@Valid @ModelAttribute("user") User user, BindingResult result)
			throws UserNotFoundException {
		if (result.hasErrors()) {
			return "/user/form.jsp";
		}
		userDao.update(user);
		return "redirect:/user";
	}
}