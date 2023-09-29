package ru.practicum.server.compilation.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Size;
import java.util.List;

@Value
@Builder
public class UpdateCompilationRequest {

    List<Integer> events;
    Boolean pinned;
    @Size(min = 1, max = 50)
    String title;
}