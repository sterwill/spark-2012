package org.tailfeather.service;

import org.tailfeather.dao.UserDao;
import org.tailfeather.entity.User;
import org.tailfeather.exceptions.UserNotFoundException;

public interface UserService {
	public User create(UserDao newObject);

	public User delete(Long id) throws UserNotFoundException;

	public Iterable<User> findAll();

	public User findById(Long id);
	
	public User findByEmail(String email);

	public User update(UserDao updated) throws UserNotFoundException;
}
