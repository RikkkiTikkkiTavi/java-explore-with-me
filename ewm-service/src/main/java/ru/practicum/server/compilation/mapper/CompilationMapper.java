package ru.practicum.server.compilation.mapper;

import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.dto.UpdateCompilationRequest;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.event.dto.EventShortDto;
import ru.practicum.server.event.mapper.EventMapper;
import ru.practicum.server.event.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation newDtoToCompilation(NewCompilationDto dto, List<Event> events) {
        Boolean pinned = false;
        if (dto.getPinned() != null) {
            pinned = dto.getPinned();
        }

        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(pinned)
                .events(events)
                .build();
    }

    public static Compilation updateDtoToCompilation(UpdateCompilationRequest request, Compilation compilation, List<Event> events) {
        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }
        if (events != null) {
            compilation.setEvents(events);
        }
        return compilation;
    }

    public static CompilationDto compilationToDto(Compilation compilation) {
        List<EventShortDto> events = new ArrayList<>();
        if (!compilation.getEvents().isEmpty()) {
            events = compilation.getEvents().stream().map(EventMapper::eventToShortDto)
                    .collect(Collectors.toList());
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
