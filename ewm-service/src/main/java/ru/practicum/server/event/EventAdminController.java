package ru.practicum.server.event;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.dto.UpdateEventAdminRequest;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.event.repository.AdminEventsParameters;
import ru.practicum.server.event.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("admin/events")
@AllArgsConstructor
@Validated
public class EventAdminController {

    private EventService eventService;

    @GetMapping
    public List<EventFullDto> getFilteredEvents(@RequestParam(required = false) List<Integer> users,
                                                @RequestParam(required = false) List<EventState> states,
                                                @RequestParam(required = false) List<Integer> categories,
                                                @RequestParam(defaultValue = "2022-12-22 01:01:01")
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                LocalDateTime rangeStart,
                                                @RequestParam(defaultValue = "2032-12-22 01:01:01")
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        AdminEventsParameters param = AdminEventsParameters.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        return eventService.getFilteredEvents(param, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateByAdmin(@PathVariable int eventId,
                                      @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateByAdmin(eventId, updateEventAdminRequest);
    }
}
