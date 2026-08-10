package com.example.football_tournament_api.dto.error;

public record ValidationError(
        String message,
        String rejectedField,
        String rejectedValue
) {
}
