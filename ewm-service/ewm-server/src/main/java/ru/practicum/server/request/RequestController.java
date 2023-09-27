package ru.practicum.server.request;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("users/{userId}/requests")
@AllArgsConstructor
@Validated
public class RequestController {

    RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable int userId, @RequestParam int eventId) {
        return requestService.add(eventId, userId);
    }

    @GetMapping
    public List<ParticipationRequestDto> findUserRequests(@PathVariable int userId) {
        return requestService.getUserRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable int userId, @PathVariable int requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}