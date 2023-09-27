package ru.practicum.server.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository <Compilation, Integer> {
    Page<Compilation> findAllByPinned(Boolean pinned, PageRequest pr);
}