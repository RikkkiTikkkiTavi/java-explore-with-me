package ru.practicum.server.category.validator;

import ru.practicum.server.category.dto.NewCategoryDto;
import ru.practicum.server.exception.ValidateException;

public class CategoryValidator {

    private static final int UNICODE_SPACE_ENCODING = 32;

    public static void checkCategory(NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getName() == null || newCategoryDto.getName().isEmpty()) {
            throw new ValidateException("The category must have a name");
        }

        if (newCategoryDto.getName().chars().allMatch(c -> c == UNICODE_SPACE_ENCODING)) {
            throw new ValidateException("Name cannot consist only of spaces");
        }
    }
}
