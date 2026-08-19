package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.event.TournamentMatchesCreatedEvent;
import com.example.football_tournament_api.exception.AlreadyExistsException;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.repository.MatchRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.repository.TournamentTeamRepository;
import com.example.football_tournament_api.service.MatchGeneratingStrategy;
import com.example.football_tournament_api.service.TournamentFixtureService;
import com.example.football_tournament_api.service.TournamentTeamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentFixtureServiceImpl implements TournamentFixtureService {
    private final MatchGeneratingStrategy matchGeneratingStrategy;
    private final TournamentRepository tournamentRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final MatchRepository matchRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public void generateMatches(Integer tournamentId) {
        if(matchRepository.existsByTournamentId(tournamentId)){
            throw new AlreadyExistsException("Matches of this tournament have already been generated");
        }
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));

        List<Team> registeredTeams = tournamentTeamRepository.findTeamsByTournamentId(tournamentId);
        List<Match> matches = matchGeneratingStrategy.generateMatches(tournament, registeredTeams);

        List<Integer> teamIds=tournamentTeamRepository.findTeamIdsByTournamentId(tournamentId);

        applicationEventPublisher.publishEvent(new TournamentMatchesCreatedEvent(tournamentId,teamIds));

        matchRepository.saveAll(matches);
    }
}
