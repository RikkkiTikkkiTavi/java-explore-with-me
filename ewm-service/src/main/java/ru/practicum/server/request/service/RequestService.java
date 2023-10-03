package ru.practicum.server.request.service;

import ru.practicum.server.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto add(int eventId, int userId);

    List<ParticipationRequestDto> getUserRequests(int userId);

    ParticipationRequestDto cancelRequest(int userId, int requestId);
}
