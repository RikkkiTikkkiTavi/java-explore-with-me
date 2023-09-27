package ru.practicum.server.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.location.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
