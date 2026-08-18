package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.MatchMapper;
import com.example.football_tournament_api.repository.MatchRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    @Override
    public void updateMatchScore(Integer matchId, int homeTeamScore, int awayTeamScore) {

    }

@Override
    public List<MatchResponse> getAllMatches(Integer tournamentId){
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));

        if(!matchRepository.existsByTournamentId(tournamentId)){
            throw new ResourceNotFoundException("Matches have not still been generated for this tournament");
        }

        List<Match> matches=matchRepository.findByTournamentId(tournamentId);
        List<MatchResponse> response=matchMapper.toMatchResponseList(matches);
        return response;
    }




}
