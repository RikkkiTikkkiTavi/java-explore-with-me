package ru.practicum.server.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class EventShortDto {
    int id;
    String annotation;
    Category category;
    int confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    UserShortDto initiator;
    boolean paid;
    String title;
    int views;
}
