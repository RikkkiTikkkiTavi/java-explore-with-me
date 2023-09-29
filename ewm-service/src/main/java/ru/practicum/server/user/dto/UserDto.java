package ru.practicum.server.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {
    int id;
    String name;
    String email;
}
