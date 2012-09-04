package org.tailfeather.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tailfeather.ControllerHelper;
import org.tailfeather.entity.Location;
import org.tailfeather.exceptions.LocationNotFoundException;
import org.tailfeather.repository.LocationRepository;

@Component
public class LocationDao {
	private static final Sort SORT_BY_NAME_ASC = new Sort(new Sort.Order(Direction.ASC, "name"));

	@Autowired
	LocationRepository locationRepository;

	@Transactional
	public Location create(Location newObject) {
		return locationRepository.save(newObject);
	}

	@Transactional
	public void delete(String id) throws LocationNotFoundException {
		try {
			locationRepository.delete(id);
		} catch (EntityNotFoundException e) {
			throw new LocationNotFoundException(id);
		}
	}

	@Transactional
	public List<Location> findAll() {
		return new ArrayList<Location>(locationRepository.findAll(SORT_BY_NAME_ASC));
	}

	@Transactional
	public Location findById(String id) {
		return locationRepository.findOne(id);
	}

	@Transactional
	public Location update(Location location) throws LocationNotFoundException {
		try {
			return locationRepository.save(location);
		} catch (EntityNotFoundException e) {
			throw new LocationNotFoundException(location.getId());
		}
	}

	public Location getCookiedLocation(HttpServletRequest request) {
		Map<String, Cookie> cookies = ControllerHelper.map(request.getCookies());
		if (cookies.containsKey(Location.ID_COOKIE_NAME)) {
			return findById(cookies.get(Location.ID_COOKIE_NAME).getValue());
		}
		return null;
	}
}
