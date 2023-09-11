package ru.practicum.dto.hit;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class HitDto {
    String app;
    String uri;
    String ip;
    LocalDateTime created;
}
