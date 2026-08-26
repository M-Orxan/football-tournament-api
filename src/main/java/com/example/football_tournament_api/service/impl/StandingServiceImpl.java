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
                .findAllByTournamentIdOrderByPointsDescGoalDifferenceDescGoalsForDesc(tournamentId);

        List<Match> finishedMatches=matchRepository
                .findByTournamentIdAndStatus(tournamentId,MatchStatus.Finished);

        for(Standing standing:standings){
            resetStanding(standing);
        }

        Map<Integer, Standing> standingMap = standings.stream()
                .collect(Collectors.toMap(s -> s.getTeam().getId(), s -> s));

        for(Match match:finishedMatches){
            Standing homeTeamStanding=standingMap.get(match.getHomeTeam().getId());
            Standing awayTeamStanding=standingMap.get(match.getAwayTeam().getId());
            if(match.getHomeTeamScore()>match.getAwayTeamScore()){
                homeTeamStanding.setWon(homeTeamStanding.getWon()+1);
                awayTeamStanding.setLost(awayTeamStanding.getLost()+1);
                homeTeamStanding.setPoints(homeTeamStanding.getPoints()+3);
            }else if(match.getAwayTeamScore()>match.getHomeTeamScore()){
                awayTeamStanding.setWon(awayTeamStanding.getWon()+1);
                homeTeamStanding.setLost(homeTeamStanding.getLost()+1);
                awayTeamStanding.setPoints(awayTeamStanding.getPoints()+3);
            }
            else{
                homeTeamStanding.setDrawn(homeTeamStanding.getDrawn()+1);
                awayTeamStanding.setDrawn(awayTeamStanding.getDrawn()+1);
                homeTeamStanding.setPoints(homeTeamStanding.getPoints()+1);
                awayTeamStanding.setPoints(awayTeamStanding.getPoints()+1);
            }

            homeTeamStanding.setGoalsFor(homeTeamStanding.getGoalsFor()+match.getHomeTeamScore());
            homeTeamStanding.setGoalsAgainst(homeTeamStanding.getGoalsAgainst()+match.getAwayTeamScore());
            homeTeamStanding.setGoalDifference(homeTeamStanding.getGoalsFor()- homeTeamStanding.getGoalsAgainst());
            homeTeamStanding.setPlayed(homeTeamStanding.getPlayed()+1);

            awayTeamStanding.setGoalsFor(awayTeamStanding.getGoalsFor()+match.getAwayTeamScore());
            awayTeamStanding.setGoalsAgainst(awayTeamStanding.getGoalsAgainst()+match.getHomeTeamScore());
            awayTeamStanding.setGoalDifference(awayTeamStanding.getGoalsFor()- awayTeamStanding.getGoalsAgainst());
            awayTeamStanding.setPlayed(awayTeamStanding.getPlayed()+1);
        }
    }

    private void resetStanding(Standing standing) {
        standing.setPlayed(0);
        standing.setWon(0);
        standing.setDrawn(0);
        standing.setLost(0);
        standing.setGoalsFor(0);
        standing.setGoalsAgainst(0);
        standing.setGoalDifference(0);
        standing.setPoints(0);
    }




}
