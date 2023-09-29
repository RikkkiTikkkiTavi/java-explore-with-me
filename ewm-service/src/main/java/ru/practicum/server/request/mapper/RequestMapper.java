package ru.practicum.server.request.mapper;

import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.model.ParticipationRequest;

public class RequestMapper {
    public static ParticipationRequestDto requestToRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto
                .builder()
                .id(request.getId())
                .created(request.getCreated())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .event(request.getEvent().getId())
                .build();
    }
}
