package ru.practicum.server.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    String status;
    String reason;
    String message;
    LocalDateTime timestamp;
}