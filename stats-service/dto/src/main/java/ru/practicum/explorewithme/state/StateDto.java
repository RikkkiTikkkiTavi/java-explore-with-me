package ru.practicum.explorewithme.state;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StateDto {
    String app;
    String uri;
    int hits;
}
