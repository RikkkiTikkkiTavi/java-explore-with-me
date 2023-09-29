package ru.practicum.server.event;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.dto.*;
import ru.practicum.server.event.service.EventService;
import ru.practicum.server.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users/{userId}/events")
@AllArgsConstructor
@Validated
public class EventUserController {

    EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable("userId") int userId,
                               @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.create(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable("userId") int userId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable("userId") int userId,
                                     @PathVariable("eventId") int eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable("userId") int userId,
                               @PathVariable("eventId") int eventId,
                               @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return eventService.update(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable("userId") int userId,
                                                                  @PathVariable("eventId") int eventId) {
        return eventService.getParticipationRequests(userId, eventId);
    }


    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestStatus(@PathVariable("userId") int userId,
                                                                   @PathVariable("eventId") int eventId,
                                                                   @RequestBody @Valid EventRequestStatusUpdateRequest request) {
        return eventService.updateEventRequestStatus(userId, eventId, request);
    }
}
