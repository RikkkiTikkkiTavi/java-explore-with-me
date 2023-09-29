package ru.practicum.server.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findAllByIdIn(List<Integer> ids, PageRequest pr);
}
