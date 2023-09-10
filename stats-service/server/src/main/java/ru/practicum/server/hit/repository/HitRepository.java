package ru.practicum.server.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Integer> {

    List<Hit> findAllByUriInAndCreatedBetween(List<String> uris, LocalDateTime start, LocalDateTime end);

    List<Hit> findAllByCreatedBetween(LocalDateTime start, LocalDateTime end);
}