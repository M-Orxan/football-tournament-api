package com.example.football_tournament_api.dto.tournament;

import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;

public record TournamentCreateRequest(
        @NotBlank(message = "Tournament name is required")
        String name,
//        @NotBlank(message = "Tournament type is required")
//        String type
        @NotBlank(message = "Tournament type is required")
        @ValidEnum(enumClass = TournamentType.class, message = "Invalid type provided")
        String type
) {
}
