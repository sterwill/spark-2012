package org.tailfeather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tailfeather.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public Iterable<User> findByEmail(String email);

	/* A custom query */
	// @Query("SELECT p FROM Person p WHERE LOWER(p.lastName) = LOWER(:lastName)")
	// public List<Person> find(@Param("lastName") String lastName);
}
