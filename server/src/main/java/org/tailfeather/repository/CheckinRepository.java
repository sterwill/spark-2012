package org.tailfeather.repository;

import java.util.List;

import javax.persistence.OrderBy;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.tailfeather.entity.Checkin;

@Component
public interface CheckinRepository extends JpaRepository<Checkin, String> {

	@Override
	@OrderBy("time")
	public List<Checkin> findAll();

	@Override
	@OrderBy("time")
	public List<Checkin> findAll(Sort sort);

	@Query("SELECT c FROM Checkin c WHERE c.user.id = ?1")
	@OrderBy("time")
	public Iterable<Checkin> findByUser(Sort sort, String userId);

	@Query("SELECT c FROM Checkin c WHERE c.location.id = ?1")
	@OrderBy("time")
	public Iterable<Checkin> findByLocation(Sort sort, String locationId);

	/* A custom query */
	// @Query("SELECT p FROM Person p WHERE LOWER(p.lastName) = LOWER(:lastName)")
	// public List<Person> find(@Param("lastName") String lastName);
}
