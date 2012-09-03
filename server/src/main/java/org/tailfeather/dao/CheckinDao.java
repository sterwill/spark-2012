package org.tailfeather.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tailfeather.entity.Checkin;
import org.tailfeather.exceptions.CheckinNotFoundException;
import org.tailfeather.repository.CheckinRepository;

@Component
public class CheckinDao {
	@Autowired
	CheckinRepository checkinRepository;

	@Transactional
	public Checkin create(Checkin newObject) {
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
		return new ArrayList<Checkin>(checkinRepository.findAll());
	}

	@Transactional
	public Checkin findById(String id) {
		return checkinRepository.findOne(id);
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