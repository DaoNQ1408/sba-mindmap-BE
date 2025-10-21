package com.sbaproject.sbamindmap.mapper;

import com.sbaproject.sbamindmap.dto.request.CommunityRequest;
import com.sbaproject.sbamindmap.dto.response.CommunityResponse;
import com.sbaproject.sbamindmap.entity.Community;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CommunityMapper {
    Community toEntity(CommunityRequest request);
    CommunityResponse toResponse(Community community);
}
