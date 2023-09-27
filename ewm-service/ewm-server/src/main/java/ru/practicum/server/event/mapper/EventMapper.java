package ru.practicum.server.event.mapper;

import ru.practicum.server.event.dto.*;
import ru.practicum.server.category.mapper.CategoryMapper;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.exception.ConflictException;
import ru.practicum.server.user.mapper.UserMapper;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;

public class EventMapper {
    public static Event newEventDtoToEvent(NewEventDto dto, Category category, User initiator) {
        boolean requestModeration = true;
        if (dto.getRequestModeration() != null) {
            requestModeration = dto.getRequestModeration();
        }
        return Event.builder()
                .annotation(dto.getAnnotation())
                .category(category)
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .publishedOn(null)
                .createdOn(LocalDateTime.now())
                .location(dto.getLocation())
                .paid(dto.isPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(requestModeration)
                .title(dto.getTitle())
                .initiator(initiator)
                .state(EventState.PENDING)
                .confirmedRequests(0)
                .build();
    }

    public static EventFullDto eventToEventFullDto(Event event, int views) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static EventShortDto eventToShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public static Event updateEventByUser(Event event, UpdateEventUserRequest request, Category category) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Published events cannot be updated");
        }
        if (request.getStateAction() != null) {
            if (request.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            }
            if (request.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getParticipantLimit() != 0) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getPaid() != null) {
            event.setRequestModeration(request.getPaid());
        }
        if (category != null) {
            event.setCategory(category);
        }
        return event;
    }

    public static Event updateEventByAdmin(Event event, UpdateEventAdminRequest request, Category category) {
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ConflictException("Only pending events can be updated");
        }
        if (request.getStateAction() != null) {
            if (request.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                if (event.getState().equals(EventState.PUBLISHED)) {
                    throw new ConflictException("The event has already been published");
                }
                event.setPublishedOn(LocalDateTime.now());
                event.setState(EventState.PUBLISHED);
            }
            if (request.getStateAction().equals(StateAction.REJECT_EVENT)) {
                if (event.getState().equals(EventState.PUBLISHED)) {
                    throw new ConflictException("Only unpublished events can be rejected");
                }
                event.setState(EventState.REJECTED);
            }
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        if (request.getParticipantLimit() != 0) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (category != null) {
            event.setCategory(category);
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        return event;
    }
}
