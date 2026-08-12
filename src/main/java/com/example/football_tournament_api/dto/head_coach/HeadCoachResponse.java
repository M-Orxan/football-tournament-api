package com.example.football_tournament_api.dto.head_coach;

import java.time.LocalDateTime;

public record HeadCoachResponse(
        Integer id,
        String name,
        LocalDateTime createdAt
) {
}
