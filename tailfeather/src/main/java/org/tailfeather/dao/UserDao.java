package org.tailfeather.dao;

import java.util.ArrayList;
import java.util.List;

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
		User user = new User(newObject.getEmail(), newObject.getFullName());
		return userRepository.save(user);
	}

	@Transactional(rollbackFor = UserNotFoundException.class)
	public User delete(String id) throws UserNotFoundException {
		User deleted = userRepository.findOne(id);

		if (deleted == null) {
			throw new UserNotFoundException(id);
		}

		userRepository.delete(deleted);
		return deleted;
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
	public User findByEmail(String email) {
		Iterable<User> users = userRepository.findByEmail(email);
		if (users == null) {
			return null;
		}
		return users.iterator().next();
	}

	@Transactional(rollbackFor = UserNotFoundException.class)
	public User update(User user) throws UserNotFoundException {
		User existing = userRepository.findOne(user.getId());

		if (existing == null) {
			throw new UserNotFoundException(user.getId());
		}

		existing.update(user);
		return userRepository.save(existing);
	}
}
