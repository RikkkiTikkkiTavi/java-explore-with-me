package ru.practicum.server.hit.service;

import ru.practicum.dto.hit.HitDto;
import ru.practicum.dto.state.StateDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    void add(HitDto hitDto);

    List<StateDto> getState(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
