package ru.practicum.server.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.location.model.Location;
import ru.practicum.server.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class EventFullDto {
    int id;
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    UserShortDto initiator;
    Location location;
    boolean paid;
    int participantLimit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
    boolean requestModeration;
    EventState state;
    String title;
    int views;
}
