package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.standing.StandingResponse;
import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Standing;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.enums.MatchStatus;
import com.example.football_tournament_api.event.MatchFinishedEvent;
import com.example.football_tournament_api.event.TournamentMatchesCreatedEvent;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.StandingMapper;
import com.example.football_tournament_api.repository.MatchRepository;
import com.example.football_tournament_api.repository.StandingRepository;
import com.example.football_tournament_api.repository.TeamRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.service.StandingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StandingServiceImpl implements StandingService {
    private final StandingRepository standingRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final StandingMapper standingMapper;
    private final MatchRepository matchRepository;

    @Override
    @EventListener
    @Transactional

    public void onMatchFinished(MatchFinishedEvent event) {
        recalculateTournamentStandings(event.tournamentId());
    }

    @Override
    @EventListener
    public void onTournamentMatchesCreated(TournamentMatchesCreatedEvent event) {

        if(standingRepository.existsByTournamentId(event.tournamentId())){
            throw new IllegalStateException("Standing for this tournament is already initialized.");
        }

        Tournament tournamentProxy=tournamentRepository.getReferenceById(event.tournamentId());
        List<Standing> standingsToBeSaved=new ArrayList<>();
        for(Integer teamId:event.teamIds()){
            Team teamProxy=teamRepository.getReferenceById(teamId);
            Standing standing=new Standing();
            standing.setTournament(tournamentProxy);
            standing.setTeam(teamProxy);
            standingsToBeSaved.add(standing);
        }
        standingRepository.saveAll(standingsToBeSaved);
    }

    @Override
    public List<StandingResponse> getAll(Integer tournamentId) {
        List<Standing> standings=standingRepository.findAllByTournamentIdOrderByPointsDescGoalDifferenceDescGoalsForDesc(tournamentId);

        return standingMapper.toResponse(standings);
    }



    @Override
    @Transactional
    public void recalculateTournamentStandings(Integer tournamentId){
        List<Standing> standings=standingRepository
                .findAllByTournamentId(tournamentId);

        List<Match> finishedMatches=matchRepository
                .findByTournamentIdAndStatus(tournamentId,MatchStatus.Finished);

        for(Standing standing:standings){
            standing.reset();
        }

        Map<Integer, Standing> standingMap = standings.stream()
                .collect(Collectors.toMap(s -> s.getTeam().getId(), s -> s));

        for(Match match:finishedMatches){
            Standing homeTeamStanding=standingMap.get(match.getHomeTeam().getId());
            Standing awayTeamStanding=standingMap.get(match.getAwayTeam().getId());

            homeTeamStanding.applyMatchResult(match.getHomeTeamScore(),match.getAwayTeamScore());
            awayTeamStanding.applyMatchResult(match.getAwayTeamScore(),match.getHomeTeamScore());
        }
    }
}
