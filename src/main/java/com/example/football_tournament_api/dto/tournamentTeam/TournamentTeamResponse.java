package com.example.football_tournament_api.dto.tournamentTeam;

import java.time.LocalDateTime;

public record TournamentTeamResponse(
        Integer id,
        Integer teamId,
        String teamName,
        Integer tournamentId,
        String tournamentName,
        LocalDateTime createdAt

) {
}
