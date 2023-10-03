package ru.practicum.server.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
