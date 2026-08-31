package com.example.football_tournament_api.mapper;

import com.example.football_tournament_api.dto.team.TeamSimpleResponse;
import com.example.football_tournament_api.dto.tournament.TournamentStatResponse;
import com.example.football_tournament_api.entity.Standing;
import com.example.football_tournament_api.enums.AggregationType;
import com.example.football_tournament_api.enums.StatType;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class StatisticsMapper {

     public TournamentStatResponse toTournamentStatResponse(Integer tournamentId,
                                                            StatType statType,
                                                            AggregationType aggregationType,
                                                            Double value,
                                                            List<Standing> standings){
          List<TeamSimpleResponse> teams=standings.stream()
                  .map(s->new TeamSimpleResponse(s.getTeam().getId(),s.getTeam().getName())).toList();

          return new TournamentStatResponse(tournamentId,statType,aggregationType,value,teams);
     }
}
