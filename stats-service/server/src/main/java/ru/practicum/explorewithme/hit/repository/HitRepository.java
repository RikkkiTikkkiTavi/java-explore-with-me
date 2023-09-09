package ru.practicum.explorewithme.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Integer> {

    List<Hit> findAllByUriInAndCreatedBetween(List<String> uris, LocalDateTime start, LocalDateTime end);

    List<Hit> findAllByCreatedBetween(LocalDateTime start, LocalDateTime end);
}
