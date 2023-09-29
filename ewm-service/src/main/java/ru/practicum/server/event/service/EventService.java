package ru.practicum.server.event.service;

import ru.practicum.server.event.dto.*;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto create(int userId, NewEventDto newEventDto);

    List<EventShortDto> getUserEvents(int userId, int from, int size);

    EventFullDto getUserEvent(int userId, int eventId);

    EventFullDto update(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getParticipationRequests(int userId, int eventId);

    EventRequestStatusUpdateResult updateEventRequestStatus(int userId, int eventId,
                                                            EventRequestStatusUpdateRequest request);

    List<EventFullDto> getFilteredEvents(List<Integer> users, List<EventState> states, List<Integer> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateByAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getPublicEvents(String text, List<Integer> category, Boolean paid, Boolean onlyAvailable,
                                        String sort, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from,
                                        int size, HttpServletRequest request);

    EventFullDto getEventInfo(int eventId, HttpServletRequest request);
}
