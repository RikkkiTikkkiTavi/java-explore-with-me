package ru.practicum.explorewithme.hit.service;

import ru.practicum.explorewithme.hit.HitDto;
import ru.practicum.explorewithme.state.StateDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    void add(HitDto hitDto);

    List<StateDto> getState(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
