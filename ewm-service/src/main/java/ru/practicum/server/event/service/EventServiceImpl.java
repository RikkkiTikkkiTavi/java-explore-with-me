package ru.practicum.server.event.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.hit.HitDto;
import ru.practicum.dto.stat.StatDto;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.repository.CategoryRepository;
import ru.practicum.server.event.dto.*;
import ru.practicum.server.event.mapper.EventMapper;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.event.repository.AdminEventsParameters;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.event.repository.PublicEventsParameters;
import ru.practicum.server.event.validator.EventValidator;
import ru.practicum.server.exception.ConflictException;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.location.repository.LocationRepository;
import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.mapper.RequestMapper;
import ru.practicum.server.request.model.ParticipationRequest;
import ru.practicum.server.request.model.Status;
import ru.practicum.server.request.repository.RequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private LocationRepository locationRepository;
    private RequestRepository requestRepository;
    private StatsClient statsClient;

    private static final long IGNORE_NANOS = 1;

    @Transactional
    @Override
    public EventFullDto create(int userId, NewEventDto newEventDto) {
        EventValidator.checkEventDate(newEventDto);

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        locationRepository.save(newEventDto.getLocation());
        Event event = eventRepository.save(EventMapper.newEventDtoToEvent(newEventDto, category, user));
        return EventMapper.eventToEventFullDto(event, 0);
    }

    @Override
    public List<EventShortDto> getUserEvents(int userId, int from, int size) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        PageRequest pr = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Event> events = eventRepository.findAllByInitiator_Id(userId, pr);
        return events.stream().map(EventMapper::eventToShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getUserEvent(int userId, int eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        Event event = eventRepository.findFirstByIdAndInitiator_Id(eventId, userId);
        return EventMapper.eventToEventFullDto(event, getStats(event));
    }

    @Transactional
    @Override
    public EventFullDto update(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        EventValidator.checkEventDate(updateEventUserRequest);
        Category category = null;
        if (updateEventUserRequest.getCategory() != 0) {
            category = categoryRepository.findById(updateEventUserRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
        }
        Event updatedEvent = EventMapper.updateEventByUser(event, updateEventUserRequest, category);

        return EventMapper.eventToEventFullDto(updatedEvent, getStats(event));
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(int userId, int eventId) {
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        List<ParticipationRequest> requests = requestRepository.findAllByEvent_Id(eventId);
        return requests.stream().map(RequestMapper::requestToRequestDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateEventRequestStatus(int userId, int eventId,
                                                                   EventRequestStatusUpdateRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        List<Integer> ids = request.getRequestIds();

        if (request.getStatus() == null) {
            throw new ConflictException("Status must not be null");
        }
        if (requestRepository.findAllByIdInAndStatus(ids, Status.CONFIRMED).size() > 0 && request.getStatus()
                .equals(Status.REJECTED)) {
            throw new ConflictException("Confirmed applications cannot be rejected");
        }
        if (!event.isRequestModeration()) {
            throw new ConflictException("The event does not require pre-moderation");
        }
        if (request.getStatus().equals(Status.CONFIRMED)) {
            int emptySlots = event.getParticipantLimit() - event.getConfirmedRequests();
            if (emptySlots == 0) {
                throw new ConflictException("Order limit reached");
            }

            if (event.getParticipantLimit() == 0 || emptySlots >= request.getRequestIds().size()) {
                List<Integer> confirmedRequests = request.getRequestIds();
                requestRepository.updateRequests(confirmedRequests, Status.CONFIRMED, Status.PENDING);
                event.setConfirmedRequests(event.getConfirmedRequests() + confirmedRequests.size());
                return getResult(confirmedRequests, new ArrayList<>());
            } else {
                List<Integer> confirmed = new ArrayList<>();
                List<Integer> rejected = new ArrayList<>();
                for (int id : request.getRequestIds()) {
                    if (emptySlots > 0) {
                        confirmed.add(id);
                        emptySlots--;
                    } else {
                        rejected.add(id);
                    }
                }
                if (!confirmed.isEmpty()) {
                    requestRepository.updateRequests(confirmed, Status.CONFIRMED, Status.PENDING);
                    event.setConfirmedRequests(event.getConfirmedRequests() + confirmed.size());
                }
                if (!rejected.isEmpty()) {
                    requestRepository.updateRequests(rejected, Status.REJECTED, Status.PENDING);
                }
                return getResult(confirmed, rejected);
            }
        } else if (request.getStatus().equals(Status.REJECTED)) {
            List<Integer> rejectedRequests = request.getRequestIds();
            requestRepository.updateRequests(rejectedRequests, Status.REJECTED, Status.PENDING);
            return getResult(new ArrayList<>(), rejectedRequests);
        } else {
            throw new ConflictException("Unknown status");
        }
    }

    private EventRequestStatusUpdateResult getResult(List<Integer> confirmed, List<Integer> rejected) {
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        if (!confirmed.isEmpty()) {
            confirmedRequests = requestRepository.findAllByIdInAndStatus(confirmed, Status.CONFIRMED).stream()
                    .map(RequestMapper::requestToRequestDto).collect(Collectors.toList());
        }
        if (!rejected.isEmpty()) {
            rejectedRequests = requestRepository.findAllByIdInAndStatus(rejected, Status.REJECTED).stream()
                    .map(RequestMapper::requestToRequestDto).collect(Collectors.toList());
        }
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    @Override
    public List<EventFullDto> getFilteredEvents(AdminEventsParameters param, int from, int size) {
        PageRequest pr = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return eventRepository.getFilteredEvents(param, pr).stream()
                .map(e -> EventMapper.eventToEventFullDto(e, getStats(e))).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto updateByAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));

        Category category = null;
        if (updateEventAdminRequest.getCategory() != 0) {
            category = categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
        }
        if (updateEventAdminRequest.getLocation() != null) {
            locationRepository.save(updateEventAdminRequest.getLocation());
        }
        Event updatedEvent = EventMapper.updateEventByAdmin(event, updateEventAdminRequest, category);
        EventValidator.checkPublishedTime(updatedEvent);

        return EventMapper.eventToEventFullDto(updatedEvent, getStats(event));
    }

    @Override
    public List<EventShortDto> getPublicEvents(PublicEventsParameters param, int from, int size,
                                               HttpServletRequest request) {
        EventValidator.checkParameters(param);
        PageRequest pr = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, param.getSort()));
        if (param.getOnlyAvailable()) {
            Page<Event> availableEvents = eventRepository.getAvailableEvents(param, pr);
            createHit(request);
            return availableEvents.stream().map(EventMapper::eventToShortDto).collect(Collectors.toList());
        } else {
            Page<Event> allEvents = eventRepository.getPublicEvents(param, pr);
            createHit(request);
            return allEvents.stream().map(EventMapper::eventToShortDto).collect(Collectors.toList());
        }
    }

    @Override
    public EventFullDto getEventInfo(int eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("State must be published");
        }
        createHit(request);
        return EventMapper.eventToEventFullDto(event, getStats(event));
    }

    private void createHit(HttpServletRequest request) {
        statsClient.addHit(HitDto.builder()
                .app("ewm-main-service")
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .created(LocalDateTime.now())
                .build());
    }

    private int getStats(Event event) {
        String uri = "/events/" + event.getId();
        String[] uris = new String[]{uri};
        List<StatDto> stats =
                statsClient.getStats(event.getCreatedOn(), LocalDateTime.now().plusSeconds(IGNORE_NANOS), uris, true);
        if (stats.size() > 0) {
            return stats.get(0).getHits();
        }
        return 0;
    }
}
