package com.example.football_tournament_api.dto.match;

public record MatchResponse(
        Integer id,
        Integer homeTeamId,
        String homeTeam,
        Integer awayTeamId,
        String awayTeam,
        Integer homeTeamScore,
        Integer awayTeamScore,
        Integer tournamentId
) {
}
