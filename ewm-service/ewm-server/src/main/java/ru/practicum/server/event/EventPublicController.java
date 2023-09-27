package ru.practicum.server.event;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.dto.EventShortDto;
import ru.practicum.server.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("events")
@AllArgsConstructor
@Validated
public class EventPublicController {

    StatsClient statsClient;

    private EventService eventService;

    @GetMapping
    public List<EventShortDto> getPublicEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Integer> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(required = false) String sort,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               LocalDateTime rangeStart,
                                               @RequestParam(defaultValue = "2032-12-22 01:01:01")
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {
        return eventService.getPublicEvents(text, categories, paid, onlyAvailable, sort, rangeStart, rangeEnd, from,
                size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventInfo(@PathVariable int id, HttpServletRequest request) {
        return eventService.getEventInfo(id, request);
    }
}
