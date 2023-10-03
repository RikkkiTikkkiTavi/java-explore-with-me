package ru.practicum.server.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.server.request.model.Status;

import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder
public class EventRequestStatusUpdateRequest {
    @NotNull
    List<Integer> requestIds;
    @NotNull
    Status status;
}
