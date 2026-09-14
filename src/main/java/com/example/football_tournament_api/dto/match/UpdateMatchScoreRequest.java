package com.example.football_tournament_api.dto.match;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateMatchScoreRequest(

        @NotNull(message = "Home team score is required")
        @PositiveOrZero(message = "Invalid score")
        Integer homeTeamScore,
        @NotNull(message = "Away team score is required")
        @PositiveOrZero(message = "Invalid score")
        Integer awayTeamScore
) {
}
