package com.example.football_tournament_api.dto.team;

import jakarta.validation.constraints.NotBlank;

public record TeamCreateRequest(
        @NotBlank(message = "Team name is required")
        String name

) {
}
