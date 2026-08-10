package com.example.football_tournament_api.mapper;

import com.example.football_tournament_api.dto.player.PlayerCreateRequest;
import com.example.football_tournament_api.dto.player.PlayerResponse;
import com.example.football_tournament_api.dto.player.PlayerUpdateRequest;
import com.example.football_tournament_api.entity.Player;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PlayerMapper {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "deleted",ignore = true)
    @Mapping(target = "deletedAt",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    @Mapping(target = "team",ignore = true)
    Player toPlayer(PlayerCreateRequest request);

    @Mapping(target = "teamId", source = "team.id")
    PlayerResponse toPlayerResponse(Player player);


    List<PlayerResponse> toResponselist(List<Player> players);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "deleted",ignore = true)
    @Mapping(target = "deletedAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "team",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PlayerUpdateRequest dto, @MappingTarget Player player);

}
