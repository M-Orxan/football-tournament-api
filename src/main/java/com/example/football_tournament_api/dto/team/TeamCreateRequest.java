package com.example.football_tournament_api.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record TeamCreateRequest(
        @NotBlank(message = "Team name is required")
        String name,
        @NotNull(message = "Head coach id is required")
        Integer headCoachId

) {
}
