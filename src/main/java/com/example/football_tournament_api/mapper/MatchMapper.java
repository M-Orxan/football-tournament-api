package com.example.football_tournament_api.mapper;

import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)

public interface MatchMapper {
    @Mapping(target = "homeTeamId", source = "homeTeam.id")
    @Mapping(target = "awayTeamId", source = "awayTeam.id")
    @Mapping(target = "tournamentId", source = "tournament.id")
    MatchResponse toMatchResponse(Match match);

    List<MatchResponse> toMatchResponseList(List<Match> matches);
}
