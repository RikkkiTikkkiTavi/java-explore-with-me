package ru.practicum.server.hit.service;

import ru.practicum.dto.hit.HitDto;
import ru.practicum.dto.stat.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    void add(HitDto hitDto);

    List<StatDto> getState(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
