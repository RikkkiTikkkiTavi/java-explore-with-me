package ru.practicum.server.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.server.location.model.Location;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Value
@Builder
@Jacksonized
public class NewEventDto {
    @NotNull
    @Size(min = 20, max = 2000)
    String annotation;
    @NotNull
    int category;
    @Size(min = 20, max = 7000)
    @NotNull
    String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @NotNull
    Location location;
    boolean paid;
    int participantLimit;
    Boolean requestModeration;
    @NotNull
    @Size(min = 3, max = 120)
    String title;
}
