package ru.practicum.server.request.validator;

import ru.practicum.server.event.model.EventState;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.exception.ConflictException;
import ru.practicum.server.request.model.ParticipationRequest;
import ru.practicum.server.user.model.User;

public class RequestValidator {
    public static void checkRequest(Event event, User requestor, ParticipationRequest request) {
        if (event.getInitiator().getId() == requestor.getId()) {
            throw new ConflictException("The event initiator cannot send a request");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("You cannot participate in an unpublished event");
        }
        if(event.getParticipantLimit() != 0) {
            if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                throw new ConflictException("Request limit reached");
            }
        }
        if (request != null) {
            throw new ConflictException("attempt to duplicate request");
        }
    }
}
