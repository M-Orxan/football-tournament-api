package com.example.football_tournament_api.dto.error;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timeStamp,
        int code,
        String error,
        String message,
        List<ValidationError> validationErrors
) {
}
