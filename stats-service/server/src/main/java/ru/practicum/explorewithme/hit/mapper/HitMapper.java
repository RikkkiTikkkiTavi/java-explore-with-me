package ru.practicum.explorewithme.hit.mapper;

import ru.practicum.explorewithme.hit.HitDto;
import ru.practicum.explorewithme.hit.model.Hit;

import java.time.LocalDateTime;

public class HitMapper {

    public static Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .ip(hitDto.getIp())
                .uri(hitDto.getUri())
                .created(LocalDateTime.now())
                .build();
    }
}
