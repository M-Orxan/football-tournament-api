package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.exception.InvalidTournamentTypeException;
import com.example.football_tournament_api.service.FixtureGeneratorStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FixtureGeneratorStrategyFactory {
    private final Map<TournamentType, FixtureGeneratorStrategy> strategies;

    public FixtureGeneratorStrategyFactory(List<FixtureGeneratorStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(FixtureGeneratorStrategy::getType, strategy -> strategy));
    }

    public FixtureGeneratorStrategy getStrategy(TournamentType type) {
        FixtureGeneratorStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new InvalidTournamentTypeException("Invalid tournament type ");
        }
        return strategy;
    }
}
