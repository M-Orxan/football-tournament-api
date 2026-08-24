package com.example.football_tournament_api.mapper;

import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.dto.match.UpdateMatchScoreRequest;
import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.enums.MatchStatus;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)

public interface MatchMapper {
    @Mapping(target = "homeTeamId", source = "homeTeam.id")
    @Mapping(target = "awayTeamId", source = "awayTeam.id")
    @Mapping(target = "tournamentId", source = "tournament.id")
    @Mapping(target = "homeTeam", source = "homeTeam.name")
    @Mapping(target = "awayTeam", source = "awayTeam.name")
    MatchResponse toMatchResponse(Match match);

    List<MatchResponse> toMatchResponseList(List<Match> matches);

    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "tournament",ignore = true)
    @Mapping(target = "homeTeam",ignore = true)
    @Mapping(target = "awayTeam",ignore = true)
    @Mapping(target = "status",ignore = true)
    @Mapping(target = "roundNumber",ignore = true)
    @Mapping(target = "winnerTeamId",ignore = true)
    void updateMatchScore(UpdateMatchScoreRequest request,@MappingTarget Match match);
}
