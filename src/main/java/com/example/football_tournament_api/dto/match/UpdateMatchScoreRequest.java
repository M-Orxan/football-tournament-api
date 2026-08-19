package com.example.football_tournament_api.dto.match;

import jakarta.validation.constraints.NotNull;

public record UpdateMatchScoreRequest(

        @NotNull(message = "Home team score is required")
        Integer homeTeamScore,
        @NotNull(message = "Away team score is required")
        Integer awayTeamScore
) {
}
