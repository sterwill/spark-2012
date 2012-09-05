package org.tailfeather.controller;

import java.io.IOException;

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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sun.jersey.api.client.ClientResponse.Status;

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
	public String createForm(Model model) {
		model.addAttribute("location", new Location());
		return "/location/form.jsp";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createSubmit(@Valid @ModelAttribute("location") Location location, BindingResult result) {
		if (result.hasErrors()) {
			return "/location/form.jsp";
		}
		locationDao.create(location);
		return "redirect:/location";
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
		if (result.hasErrors()) {
			return "/location/form.jsp";
		}
		locationDao.update(location);
		return "redirect:/location";
	}

	@RequestMapping(value = "/cookie/{id}", method = RequestMethod.GET)
	public String cookie(@PathVariable String id, Model model, HttpServletResponse response) {
		response.addCookie(createLocationCookie(id));
		model.addAttribute("currentLocation", locationDao.findById(id));
		return "showCookie.jsp";
	}

	@RequestMapping(value = "/uncookie/{id}", method = RequestMethod.GET)
	public String uncookie(@PathVariable String id, Model model, HttpServletResponse response) {
		response.addCookie(createLocationUncookie(id));
		return "showCookie.jsp";
	}

	@RequestMapping(value = "/showCookie", method = RequestMethod.GET)
	public String showCookie(Model model, HttpServletRequest request) {
		model.addAttribute("currentLocation", locationDao.getCookiedLocation(request));
		return "showCookie.jsp";
	}

	@RequestMapping(value = "/qr/{id}", method = RequestMethod.GET)
	public void qr(@PathVariable String id, HttpServletRequest request, HttpServletResponse response)
			throws WriterException, IOException {
		String uri = String.format("%s://%s:%d/location/cookie/%s", request.getScheme(), request.getServerName(),
				request.getServerPort(), id);
		// URI uri =
		// uriInfo.getAbsolutePathBuilder().path("location").path("cookie").path(id).build();
		response.setStatus(Status.OK.getStatusCode());
		response.setContentType("image/png");
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix matrix = writer.encode(uri, BarcodeFormat.QR_CODE, 200, 200);
		MatrixToImageWriter.writeToStream(matrix, "PNG", response.getOutputStream());
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