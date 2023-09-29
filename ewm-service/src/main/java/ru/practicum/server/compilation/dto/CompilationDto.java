package ru.practicum.server.compilation.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.server.event.dto.EventShortDto;

import java.util.List;

@Value
@Builder
public class CompilationDto {
    int id;
    List<EventShortDto> events;
    boolean pinned;
    String title;
}
