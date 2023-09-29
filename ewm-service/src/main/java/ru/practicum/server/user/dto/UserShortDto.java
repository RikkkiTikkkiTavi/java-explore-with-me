package ru.practicum.server.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserShortDto {
    int id;
    String name;
}
