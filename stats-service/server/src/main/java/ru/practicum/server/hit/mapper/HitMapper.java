package ru.practicum.server.hit.mapper;

import ru.practicum.dto.hit.HitDto;
import ru.practicum.server.hit.model.Hit;

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
