package ru.practicum.server.category.service;

import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    CategoryDto update(CategoryDto dto, int catId);

    void delete(int catId);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(int catId);
}
