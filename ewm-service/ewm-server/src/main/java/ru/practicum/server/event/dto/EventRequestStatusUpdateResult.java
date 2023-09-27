package ru.practicum.server.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.server.request.dto.ParticipationRequestDto;

import java.util.List;

@Value
@Builder
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests;
    List<ParticipationRequestDto> rejectedRequests;
}
