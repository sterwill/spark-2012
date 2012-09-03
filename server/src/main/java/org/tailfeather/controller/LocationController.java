package org.tailfeather.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tailfeather.dao.LocationDao;
import org.tailfeather.entity.Location;
import org.tailfeather.exceptions.LocationNotFoundException;

@Controller
@RequestMapping("/location")
public class LocationController {

	@Autowired
	private LocationDao locationDao;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, HttpServletRequest request) {
		model.addAttribute("currentLocation", locationDao.getCookiedLocation(request));
		model.addAttribute("locationList", locationDao.findAll());
		return "/location/list.jsp";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(@Valid Model model) {
		model.addAttribute("location", new Location());
		return "/location/form.jsp";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createSubmit(@Valid @ModelAttribute("location") Location location, BindingResult result) {
		locationDao.create(location);
		return "redirect:/web/location";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editForm(@PathVariable String id, @Valid Model model) {
		Location location = locationDao.findById(id);
		model.addAttribute("location", location);
		return "/location/form.jsp";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public String editSubmit(@Valid @ModelAttribute("location") Location location, BindingResult result)
			throws LocationNotFoundException {
		locationDao.update(location);
		return "redirect:/web/location";
	}

	@RequestMapping(value = "/cookie/{id}", method = RequestMethod.GET)
	public String cookie(@PathVariable String id, Model model, HttpServletResponse response) {
		model.addAttribute("location", locationDao.findById(id));
		response.addCookie(createLocationCookie(id));
		return "redirect:/web/location";
	}

	@RequestMapping(value = "/uncookie/{id}", method = RequestMethod.GET)
	public String uncookie(@PathVariable String id, Model model, HttpServletResponse response) {
		response.addCookie(createLocationUncookie(id));
		return "redirect:/web/location";
	}

	private Cookie createLocationCookie(String locationId) {
		Cookie cookie = new Cookie(Location.ID_COOKIE_NAME, locationId.trim());
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath("/");
		return cookie;
	}

	private Cookie createLocationUncookie(String locationId) {
		Cookie cookie = new Cookie(Location.ID_COOKIE_NAME, locationId.trim());
		cookie.setMaxAge(0);
		cookie.setPath("/");
		return cookie;
	}
}