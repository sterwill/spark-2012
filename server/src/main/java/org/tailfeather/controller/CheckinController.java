package org.tailfeather.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tailfeather.dao.CheckinDao;
import org.tailfeather.dao.LocationDao;
import org.tailfeather.dao.UserDao;
import org.tailfeather.entity.Checkin;
import org.tailfeather.entity.Location;
import org.tailfeather.entity.User;
import org.tailfeather.exceptions.CheckinNotFoundException;

@Controller
@RequestMapping("/checkin")
public class CheckinController {

	@Autowired
	private CheckinDao checkinDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private LocationDao locationDao;

	/**
	 * Badge QR codes point here, so it's a GET instead of a more correct POST.
	 */
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public String badge(Model model, @PathVariable(value = "userId") String userId, HttpServletRequest request) {
		User user = userDao.findById(userId);
		if (user != null) {
			model.addAttribute("user", user);
			Location location = locationDao.getCookiedLocation(request);
			if (location != null) {
				model.addAttribute("location", user);
				Checkin checkin = new Checkin();
				checkin.setUser(user);
				checkin.setLocation(location);
				checkin.setTime(new Date());
				model.addAttribute("checkin", checkinDao.create(checkin));
			}
		}
		return "/checkin/badge.jsp";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, HttpServletRequest request) {
		String locationId = request.getParameter("locationId");
		String userEmail = request.getParameter("userEmail");

		Iterable<Checkin> checkinList;
		if (locationId != null) {
			model.addAttribute("location", locationDao.findById(locationId));
			checkinList = checkinDao.findByLocation(locationId);
		} else if (userEmail != null) {
			User user = userDao.findByEmail(userEmail);
			model.addAttribute("user", user);
			checkinList = checkinDao.findByUser(user.getId());
		} else {
			checkinList = checkinDao.findAll();
		}

		model.addAttribute("checkinList", checkinList);
		return "/checkin/list.jsp";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(@Valid Model model, HttpServletRequest request) {
		Location currentLocation = locationDao.getCookiedLocation(request);
		if (currentLocation == null) {
			return "/checkin/noLocation.jsp";
		}

		model.addAttribute("currentLocation", currentLocation);
		model.addAttribute("checkin", new Checkin());
		return "/checkin/form.jsp";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createSubmit(Model model, @ModelAttribute("checkin") Checkin checkin, BindingResult result,
			HttpServletRequest request) {
		Location currentLocation = locationDao.getCookiedLocation(request);
		if (currentLocation == null) {
			return "/checkin/noLocation.jsp";
		}
		model.addAttribute("currentLocation", currentLocation);

		String userEmail = request.getParameter("userEmail");
		if (userEmail == null) {
			result.addError(new ObjectError("checkin.user", "User not found"));
			return "/checkin/form.jsp";
		}
		model.addAttribute("userEmail", userEmail);

		User user = userDao.findByEmail(userEmail);
		if (user == null) {
			result.addError(new ObjectError("checkin.user", "User not found"));
			return "/checkin/form.jsp";
		}

		// Ignore the model
		checkin = new Checkin();
		checkin.setUser(user);
		checkin.setLocation(currentLocation);
		checkin.setTime(new Date());
		checkinDao.create(checkin);

		return "redirect:/web/checkin";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editForm(@PathVariable String id, @Valid Model model) {
		Checkin checkin = checkinDao.findById(id);
		model.addAttribute("checkin", checkin);
		return "/checkin/form.jsp";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public String editSubmit(@Valid @ModelAttribute("checkin") Checkin checkin, BindingResult result)
			throws CheckinNotFoundException {
		checkinDao.update(checkin);
		return "redirect:/web/checkin";
	}
}