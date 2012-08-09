package org.tailfeather.repository;

import org.springframework.data.repository.CrudRepository;
import org.tailfeather.db.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
