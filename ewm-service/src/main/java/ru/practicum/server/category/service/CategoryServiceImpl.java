package ru.practicum.server.category.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.dto.NewCategoryDto;
import ru.practicum.server.category.mapper.CategoryMapper;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.repository.CategoryRepository;
import ru.practicum.server.category.validator.CategoryValidator;
import ru.practicum.server.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        CategoryValidator.checkCategory(newCategoryDto);
        Category category = categoryRepository.save(CategoryMapper.newDtoToCategory(newCategoryDto));
        return CategoryMapper.categoryToDto(category);
    }

    @Transactional
    @Override
    public CategoryDto update(CategoryDto dto, int catId) {
        Category oldCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Element with id " + catId + "not exist"));
        oldCategory.setName(dto.getName());
        return CategoryMapper.categoryToDto(oldCategory);
    }

    @Transactional
    @Override
    public void delete(int catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categories.stream().map(CategoryMapper::categoryToDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(int catId) {
        return CategoryMapper.categoryToDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Element with id " + catId + "not exist")));
    }
}