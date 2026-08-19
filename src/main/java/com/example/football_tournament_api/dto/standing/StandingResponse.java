package com.example.football_tournament_api.dto.standing;

public record StandingResponse(
        String teamName,
         int played,
         int won,
         int drawn,
         int lost,
         int goalsFor,
         int goalsAgainst,
         int goalDifference,
         int points
) {
}
