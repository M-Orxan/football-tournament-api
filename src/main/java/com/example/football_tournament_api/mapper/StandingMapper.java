package com.example.football_tournament_api.mapper;

import com.example.football_tournament_api.dto.standing.StandingResponse;
import com.example.football_tournament_api.entity.Standing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface StandingMapper {
    @Mapping(target = "teamName",source = "team.name")
    StandingResponse toResponse(Standing standing);

    List<StandingResponse> toResponse(List<Standing> standings);
}
