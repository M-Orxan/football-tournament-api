package com.example.football_tournament_api.mapper;

import com.example.football_tournament_api.dto.tournamentTeam.TournamentTeamCreateRequest;
import com.example.football_tournament_api.dto.tournamentTeam.TournamentTeamResponse;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.entity.TournamentTeam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)

public interface TournamentTeamMapper {


    @Mapping(target = "updatedAt",ignore = true)
    @Mapping(target = "deleted",ignore = true)
    @Mapping(target = "deletedAt",ignore = true)
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "tournament",source = "tournament")
    @Mapping(target = "team",source = "team")
    @Mapping(target = "createdAt",ignore = true)

    TournamentTeam toTournamentTeam(Tournament tournament, Team team);

    @Mapping(target = "teamId",source = "team.id")
    @Mapping(target = "tournamentId",source = "tournament.id")
    @Mapping(target = "tournamentName",source = "tournament.name")
    @Mapping(target = "teamName",source = "team.name")
    TournamentTeamResponse toTournamentTeamResponse(TournamentTeam tournamentTeam);


}
