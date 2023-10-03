package ru.practicum.server.comments.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;

@Value
@Builder
public class CommentDto {
    int id;
    String text;
    Event event;
    User creator;
    LocalDateTime created;
}
