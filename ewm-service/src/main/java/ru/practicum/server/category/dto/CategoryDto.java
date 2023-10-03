package ru.practicum.server.category.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder
public class CategoryDto {
    int id;

    @NotBlank
    @Size(min = 1, max = 50, message = "Name size must be less than 50")
    String name;
}
