package com.example.football_tournament_api.mapper;

import com.example.football_tournament_api.dto.statistics.TeamStatResponse;
import com.example.football_tournament_api.entity.Standing;
import com.example.football_tournament_api.enums.StatType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class StatisticsMapper {

    public TeamStatResponse toResponse(Standing standing, StatType statType){

        if(standing==null) return null;
        return new TeamStatResponse(
                standing.getTeam().getId(),
                standing.getTeam().getName(),
                statType,
                statType.extractValue(standing)
        );

    }




    public List<TeamStatResponse> toResponseList(List<Standing> standings, StatType statType){

        if(standings==null) return Collections.emptyList();
        return standings.stream().map(s->toResponse(s,statType)).toList();

    }
}
