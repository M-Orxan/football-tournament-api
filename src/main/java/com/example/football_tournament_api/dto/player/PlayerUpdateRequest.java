package com.example.football_tournament_api.dto.player;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlayerUpdateRequest(

        String name,
        Integer teamId
) {
}
