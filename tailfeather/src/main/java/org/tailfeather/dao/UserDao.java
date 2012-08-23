package org.tailfeather.dao;

import java.util.List;

import org.tailfeather.entity.User;
import org.tailfeather.exceptions.UserNotFoundException;

public interface UserDao {
	public User create(User user);

	public User delete(Long id) throws UserNotFoundException;

	public List<User> findAll();

	public User findById(Long id);

	public User findByEmail(String email);

	public User update(User updated) throws UserNotFoundException;
}
