package com.example.football_tournament_api.mapper;

import com.example.football_tournament_api.dto.tournament.TournamentCreateRequest;
import com.example.football_tournament_api.dto.tournament.TournamentResponse;
import com.example.football_tournament_api.entity.Tournament;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TournamentMapper {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "deleted",ignore = true)
    @Mapping(target = "deletedAt",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    @Mapping(target = "tournamentTeams",ignore = true)

    Tournament toEntity(TournamentCreateRequest request);

    TournamentResponse toResponse(Tournament tournament);
}
