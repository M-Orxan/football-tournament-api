package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.MatchMapper;
import com.example.football_tournament_api.repository.MatchRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.repository.TournamentTeamRepository;
import com.example.football_tournament_api.service.FixtureGeneratorStrategy;
import com.example.football_tournament_api.service.TournamentFixtureService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentFixtureServiceImpl implements TournamentFixtureService {
    private final TournamentRepository tournamentRepository;
    private final MatchMapper matchMapper;
    private final FixtureGeneratorStrategyFactory fixtureGeneratorStrategyFactory;



    @Override
    @Transactional
    public List<MatchResponse> generateFixture(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));

        TournamentType type = tournament.getType();
        FixtureGeneratorStrategy strategy=fixtureGeneratorStrategyFactory.getStrategy(type);
      List<Match> matches= strategy.generateFixture(tournament);
      return matchMapper.toMatchResponseList(matches);
    }

    @Override
    public List<MatchResponse> generateNextRound(Integer tournamentId, int currentRoundNumber) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));

        TournamentType type = tournament.getType();
        FixtureGeneratorStrategy strategy=fixtureGeneratorStrategyFactory.getStrategy(type);
        List<Match> matches= strategy.generateNextRound(tournamentId,currentRoundNumber);
        return matchMapper.toMatchResponseList(matches);
    }
}
