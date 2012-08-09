package org.tailfeather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.tailfeather.repository.UserRepository;

public class UserService {
	@Autowired
	UserRepository userRepository;
}
