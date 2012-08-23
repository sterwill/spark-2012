package org.tailfeather.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tailfeather.entity.User;
import org.tailfeather.exceptions.UserNotFoundException;
import org.tailfeather.repository.UserRepository;

@Service
public class RepositoryUserDao implements UserDao {
	@Autowired
	UserRepository userRepository;

	@Transactional
	@Override
	public User create(User newObject) {
		User user = new User(newObject.getEmail(), newObject.getFullName());
		return userRepository.save(user);
	}

	@Transactional(rollbackFor = UserNotFoundException.class)
	@Override
	public User delete(Long id) throws UserNotFoundException {
		User deleted = userRepository.findOne(id);

		if (deleted == null) {
			throw new UserNotFoundException();
		}

		userRepository.delete(deleted);
		return deleted;
	}

	@Transactional(readOnly = true)
	@Override
	public List<User> findAll() {
		return new ArrayList<User>(userRepository.findAll());
	}

	@Transactional(readOnly = true)
	@Override
	public User findById(Long id) {
		return userRepository.findOne(id);
	}

	@Transactional(readOnly = true)
	@Override
	public User findByEmail(String email) {
		Iterable<User> users = userRepository.findByEmail(email);
		if (users == null) {
			return null;
		}
		return users.iterator().next();
	}

	@Transactional(rollbackFor = UserNotFoundException.class)
	@Override
	public User update(User user) throws UserNotFoundException {
		User existing = userRepository.findOne(user.getId());

		if (existing == null) {
			throw new UserNotFoundException();
		}

		existing.update(user);
		return userRepository.save(existing);
	}
}
