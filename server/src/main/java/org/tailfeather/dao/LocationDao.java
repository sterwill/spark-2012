package org.tailfeather.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tailfeather.entity.Location;
import org.tailfeather.exceptions.LocationNotFoundException;
import org.tailfeather.repository.LocationRepository;

@Component
public class LocationDao {
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
		return new ArrayList<Location>(locationRepository.findAll());
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
}
