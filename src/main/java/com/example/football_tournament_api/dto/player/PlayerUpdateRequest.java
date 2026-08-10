package com.example.football_tournament_api.dto.player;

import jakarta.validation.constraints.NotBlank;

public record PlayerUpdateRequest(
        String name,
        Integer teamId
) {
}
