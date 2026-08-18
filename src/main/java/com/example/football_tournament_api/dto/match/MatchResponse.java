package com.example.football_tournament_api.dto.match;

public record MatchResponse(
        Integer id,
        Integer homeTeamId,
        Integer awayTeamId,
        Integer homeTeamScore,
        Integer awayTeamScore,
        Integer tournamentId
) {
}
