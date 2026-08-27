package com.example.football_tournament_api.enums;

import com.example.football_tournament_api.entity.Standing;

import java.util.function.Function;

public enum StatType {
    CLEAN_SHEETS(Standing::getCleanSheet),
    GOALS_SCORED(Standing::getGoalsFor),
    GOALS_CONCEDED(Standing::getGoalsAgainst),
    POINTS(Standing::getPoints),
    WON(Standing::getWon),
    LOST(Standing::getLost),
    DRAWN(Standing::getDrawn),
    GOAL_DIFFERENCE(Standing::getGoalDifference),
    PLAYED(Standing::getPlayed);


    private final Function<Standing, Integer> valueExtractor;

    StatType(Function<Standing, Integer> valueExtractor) {
        this.valueExtractor = valueExtractor;
    }

    public Integer extractValue(Standing standing) {
        return this.valueExtractor.apply(standing);
    }
}
