package com.example.football_tournament_api.dto.tournamentTeam;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TournamentTeamCreateRequest(
        @NotNull(message = "Tournament ID is required")
        @Positive(message = "Tournament ID mus be positive number")
        Integer tournamentId,
        @NotNull(message = "Team ID is required")
        @Positive(message = "Team ID mus be positive number")
        Integer teamId
) {
}
