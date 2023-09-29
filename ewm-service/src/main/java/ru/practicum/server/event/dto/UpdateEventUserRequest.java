package ru.practicum.server.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.server.location.model.Location;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000)
    String annotation;
    int category;
    @Size(min = 20, max = 7000)
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    Location location;
    Boolean paid;
    int participantLimit;
    Boolean requestModeration;
    StateAction stateAction;
    @Size(min = 3, max = 120)
    String title;
}
