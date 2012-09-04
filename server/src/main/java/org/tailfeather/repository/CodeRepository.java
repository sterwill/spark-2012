package org.tailfeather.repository;

import java.util.List;

import javax.persistence.OrderBy;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.tailfeather.entity.Code;

@Component
public interface CodeRepository extends JpaRepository<Code, String> {

	@Override
	@OrderBy("time")
	public List<Code> findAll();

	@Override
	@OrderBy("time")
	public List<Code> findAll(Sort sort);

	@Query("SELECT c FROM Code c WHERE c.user.id = ?1")
	@OrderBy("time")
	public Iterable<Code> findByUser(String userId);
}
