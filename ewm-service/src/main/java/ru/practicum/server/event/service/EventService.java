package ru.practicum.server.event.service;

import ru.practicum.server.event.dto.*;
import ru.practicum.server.event.repository.AdminEventsParameters;
import ru.practicum.server.event.repository.PublicEventsParameters;
import ru.practicum.server.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventFullDto create(int userId, NewEventDto newEventDto);

    List<EventShortDto> getUserEvents(int userId, int from, int size);

    EventFullDto getUserEvent(int userId, int eventId);

    EventFullDto update(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getParticipationRequests(int userId, int eventId);

    EventRequestStatusUpdateResult updateEventRequestStatus(int userId, int eventId,
                                                            EventRequestStatusUpdateRequest request);

    List<EventFullDto> getFilteredEvents(AdminEventsParameters parameters, int from, int size);

    EventFullDto updateByAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getPublicEvents(PublicEventsParameters parameters, int from, int size,
                                        HttpServletRequest request);

    EventFullDto getEventInfo(int eventId, HttpServletRequest request);
}
