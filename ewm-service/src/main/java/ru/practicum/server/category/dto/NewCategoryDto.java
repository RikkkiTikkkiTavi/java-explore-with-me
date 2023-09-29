package ru.practicum.server.category.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@JsonDeserialize(builder = NewCategoryDto.Builder.class)
public class NewCategoryDto {

    @NotBlank
    @NotNull
    @Size(max = 50, message = "Name size must be less than 50")
    String name;

    @JsonPOJOBuilder
    static class Builder {
        String name;

        Builder withName(String name) {
            this.name = name;
            return this;
        }

        public NewCategoryDto build() {
            return new NewCategoryDto(name);
        }
    }
}
