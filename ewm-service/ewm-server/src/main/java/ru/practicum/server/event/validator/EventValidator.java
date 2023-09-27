package ru.practicum.server.event.validator;

import ru.practicum.server.event.dto.NewEventDto;
import ru.practicum.server.event.dto.UpdateEventUserRequest;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.exception.ValidateException;

import java.time.LocalDateTime;

public class EventValidator {
    public static void checkEventDate(NewEventDto newEventDto) {
        LocalDateTime eventDate = newEventDto.getEventDate();
        LocalDateTime nowMinus = LocalDateTime.now().minusHours(2L);
        if(eventDate.isBefore(nowMinus)) {
            throw new ValidateException("Event start cannot be earlier than two hours from now");
        }
    }

    public static void checkEventDate(UpdateEventUserRequest request) {
        LocalDateTime eventDate = request.getEventDate();
        LocalDateTime nowMinus = LocalDateTime.now().minusHours(2L);
        if (eventDate != null && eventDate.isBefore(nowMinus)) {
            throw new ValidateException("Event start cannot be earlier than two hours from now");
        }
    }

    public static void checkPublishedTime(Event event) {
        LocalDateTime eventDate = event.getEventDate();
        LocalDateTime nowMinus = LocalDateTime.now().minusHours(1L);
        if (eventDate.isBefore(nowMinus)) {
            throw new ValidateException("Event start cannot be earlier than one hours from now");
        }
    }

    public static String checkSort(String sort) {
        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                return "eventDate";
            } else if (sort.equals("VIEWS")) {
                return "views";
            } else {
                throw new ValidateException("Unsupported comparator");
            }
        }
        return "id";
    }
}
