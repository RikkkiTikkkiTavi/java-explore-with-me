package ru.practicum.server.compilation.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Value
@Builder
public class NewCompilationDto {
    List<Integer> events;
    Boolean pinned;
    @NotBlank
    @NotNull
    @Size(min = 1, max = 50)
    String title;
}