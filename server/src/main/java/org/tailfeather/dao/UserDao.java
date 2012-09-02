package org.tailfeather.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tailfeather.entity.User;
import org.tailfeather.exceptions.UserNotFoundException;
import org.tailfeather.repository.UserRepository;

@Component
public class UserDao {
	@Autowired
	UserRepository userRepository;

	@Transactional
	public User create(User newObject) {
		return userRepository.save(newObject);
	}

	@Transactional
	public void delete(String id) throws UserNotFoundException {
		try {
			userRepository.delete(id);
		} catch (EntityNotFoundException e) {
			throw new UserNotFoundException(id);
		}
	}

	@Transactional
	public List<User> findAll() {
		return new ArrayList<User>(userRepository.findAll());
	}

	@Transactional
	public User findById(String id) {
		return userRepository.findOne(id);
	}

	@Transactional
	public User update(User user) throws UserNotFoundException {
		try {
			return userRepository.save(user);
		} catch (EntityNotFoundException e) {
			throw new UserNotFoundException(user.getId());
		}
	}

	@Transactional
	public User findByEmail(String email) {
		Iterable<User> users = userRepository.findByEmail(email);
		if (users == null) {
			return null;
		}
		return users.iterator().next();
	}
}
