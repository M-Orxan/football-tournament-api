package com.example.football_tournament_api.mapper;

import com.example.football_tournament_api.dto.head_coach.HeadCoachCreateRequest;
import com.example.football_tournament_api.dto.head_coach.HeadCoachResponse;
import com.example.football_tournament_api.dto.head_coach.HeadCoachUpdateRequest;
import com.example.football_tournament_api.dto.player.PlayerCreateRequest;
import com.example.football_tournament_api.entity.HeadCoach;
import com.example.football_tournament_api.entity.Player;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)

public interface HeadCoachMapper {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "deleted",ignore = true)
    @Mapping(target = "deletedAt",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    HeadCoach toHeadCoach(HeadCoachCreateRequest request);



    HeadCoachResponse toHeadCoachResponse(HeadCoach headCoach);

    List<HeadCoachResponse> toHeadCoachResponseList(List<HeadCoach> headCoachList);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "deleted",ignore = true)
    @Mapping(target = "deletedAt",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy= NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(HeadCoachUpdateRequest request,@MappingTarget HeadCoach headCoach);
}
