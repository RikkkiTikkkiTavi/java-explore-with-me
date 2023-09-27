package ru.practicum.server.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.request.mapper.RequestMapper;
import ru.practicum.server.request.model.ParticipationRequest;
import ru.practicum.server.request.model.Status;
import ru.practicum.server.request.repository.RequestRepository;
import ru.practicum.server.request.validator.RequestValidator;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    RequestRepository requestRepository;
    EventRepository eventRepository;
    UserRepository userRepository;

    @Transactional
    @Override
    public ParticipationRequestDto add(int eventId, int userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        ParticipationRequest request = requestRepository.findByRequester_IdAndEvent_Id(userId, eventId).orElse(null);
        RequestValidator.checkRequest(event, user, request);

        ParticipationRequest newRequest = ParticipationRequest.builder()
                .event(event)
                .requester(user)
                .created(LocalDateTime.now())
                .status(Status.PENDING)
                .build();
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            event.setConfirmedRequests(event.getConfirmedRequests()+1);
            newRequest.setStatus(Status.CONFIRMED);
        }
        return RequestMapper.requestToRequestDto(requestRepository.save(newRequest));
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(int userId) {
        userRepository.findById(userId).orElseThrow(() ->new NotFoundException( "User not found"));
        return requestRepository.findAllByRequester_Id(userId).stream()
                .map(RequestMapper::requestToRequestDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        userRepository.findById(userId).orElseThrow(() ->new NotFoundException( "User not found"));
        ParticipationRequest pr = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        pr.setStatus(Status.CANCELED);
        return RequestMapper.requestToRequestDto(pr);
    }
}