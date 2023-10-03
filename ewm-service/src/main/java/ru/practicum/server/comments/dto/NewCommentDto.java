package ru.practicum.server.comments.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Value
@JsonDeserialize(builder = NewCommentDto.Builder.class)
public class NewCommentDto {

    @NotBlank
    @NotNull
    @Size(max = 1000, message = "Text size must be less or equals than 1000")
    String text;

    @JsonPOJOBuilder
    static class Builder {
        String text;

        Builder withText(String text) {
            this.text = text;
            return this;
        }

        public NewCommentDto build() {
            return new NewCommentDto(text);
        }
    }
}
