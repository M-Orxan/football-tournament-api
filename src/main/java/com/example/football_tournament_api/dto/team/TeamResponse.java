package com.example.football_tournament_api.dto.team;

import java.time.LocalDateTime;

public record TeamResponse(
        Integer id,
        String name,
        Integer headCoachId,
        String headCoachName,
        Long playerCount,
        LocalDateTime createdAt

) {
}
