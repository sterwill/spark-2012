package org.tailfeather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tailfeather.dao.UserDao;
import org.tailfeather.entity.User;
import org.tailfeather.exceptions.UserNotFoundException;
import org.tailfeather.repository.UserRepository;

@Service
public class RepositoryUserService implements UserService {
	@Autowired
	UserRepository userRepository;

	@Transactional
	@Override
	public User create(UserDao newObject) {
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
	public Iterable<User> findAll() {
		return userRepository.findAll();
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
	public User update(UserDao updated) throws UserNotFoundException {
		User user = userRepository.findOne(updated.getId());

		if (user == null) {
			throw new UserNotFoundException();
		}

		user.setEmail(updated.getEmail());
		user.setFullName(updated.getFullName());

		return user;
	}
}
