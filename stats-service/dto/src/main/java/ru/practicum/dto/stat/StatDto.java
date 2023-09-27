package ru.practicum.dto.stat;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StatDto {
    String app;
    String uri;
    int hits;
}
