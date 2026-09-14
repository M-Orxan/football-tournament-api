package com.example.football_tournament_api.dto.head_coach;

import jakarta.validation.constraints.NotBlank;

public record HeadCoachUpdateRequest(

        String name
) {
}
