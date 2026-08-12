package com.example.football_tournament_api.mapper;


import com.example.football_tournament_api.dto.player.PlayerUpdateRequest;
import com.example.football_tournament_api.dto.team.TeamCreateRequest;
import com.example.football_tournament_api.dto.team.TeamResponse;
import com.example.football_tournament_api.dto.team.TeamUpdateRequest;
import com.example.football_tournament_api.entity.Player;
import com.example.football_tournament_api.entity.Team;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TeamMapper {



 @Mapping(target = "playerCount",ignore = true)
 @Mapping(target = "headCoachId",source = "team.headCoach.id")
 @Mapping(target = "headCoachName",source = "team.headCoach.name")
 TeamResponse toResponse(Team team);
 List<TeamResponse> toResponseList(List<Team> teams);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "deleted",ignore = true)
    @Mapping(target = "deletedAt",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    @Mapping(target = "headCoach",ignore = true)
    @Mapping(target = "players",ignore = true)
    Team toTeam(TeamCreateRequest request);


    @Mapping(target = "id",ignore = true)
    @Mapping(target = "deleted",ignore = true)
    @Mapping(target = "deletedAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "players",ignore = true)
    @Mapping(target = "headCoach",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy= NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(TeamUpdateRequest request, @MappingTarget Team team);


}
