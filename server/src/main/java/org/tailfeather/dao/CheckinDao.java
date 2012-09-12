package org.tailfeather.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tailfeather.entity.Checkin;
import org.tailfeather.exceptions.CheckinNotFoundException;
import org.tailfeather.repository.CheckinRepository;

@Component
public class CheckinDao {
	private static final Sort SORT_BY_TIME_DESC = new Sort(new Sort.Order(Direction.DESC, "time"));

	@Autowired
	CheckinRepository checkinRepository;

	@Transactional
	public Checkin create(Checkin newObject) {
		// hack: don't allow multiple checkins for same location
		Iterable<Checkin> old = checkinRepository.findByLocation(SORT_BY_TIME_DESC, newObject.getLocation().getId());
		if (old.iterator().hasNext()) {
			return newObject;
		}

		return checkinRepository.save(newObject);
	}

	@Transactional
	public void delete(String id) throws CheckinNotFoundException {
		try {
			checkinRepository.delete(id);
		} catch (EntityNotFoundException e) {
			throw new CheckinNotFoundException(id);
		}
	}

	@Transactional
	public List<Checkin> findAll() {
		return new ArrayList<Checkin>(checkinRepository.findAll(SORT_BY_TIME_DESC));
	}

	@Transactional
	public Checkin findById(String id) {
		return checkinRepository.findOne(id);
	}

	@Transactional
	public Iterable<Checkin> findByUser(String userId) {
		return checkinRepository.findByUser(SORT_BY_TIME_DESC, userId);
	}

	@Transactional
	public Iterable<Checkin> findByLocation(String locationId) {
		return checkinRepository.findByLocation(SORT_BY_TIME_DESC, locationId);
	}

	@Transactional
	public Checkin update(Checkin checkin) throws CheckinNotFoundException {
		try {
			return checkinRepository.save(checkin);
		} catch (EntityNotFoundException e) {
			throw new CheckinNotFoundException(checkin.getId());
		}

	}
}
