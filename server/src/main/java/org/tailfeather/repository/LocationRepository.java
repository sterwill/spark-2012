package org.tailfeather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.tailfeather.entity.Location;

@Component
public interface LocationRepository extends JpaRepository<Location, String> {
}
