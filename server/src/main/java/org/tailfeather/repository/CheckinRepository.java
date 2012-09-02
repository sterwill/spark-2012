package org.tailfeather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.tailfeather.entity.Checkin;
import org.tailfeather.entity.User;

@Component
public interface CheckinRepository extends JpaRepository<Checkin, String> {

	public Iterable<Checkin> findByUser(User user);

	/* A custom query */
	// @Query("SELECT p FROM Person p WHERE LOWER(p.lastName) = LOWER(:lastName)")
	// public List<Person> find(@Param("lastName") String lastName);
}
