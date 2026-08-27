package com.example.football_tournament_api.dto.statistics;

import com.example.football_tournament_api.enums.StatType;

public record TeamStatResponse(
        Integer teamId,
        String teamName,
        StatType statType,
        Integer value
) {
}
