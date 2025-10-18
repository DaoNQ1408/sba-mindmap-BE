package com.sbaproject.sbamindmap.mapper;

import com.sbaproject.sbamindmap.dto.request.CollectionRequest;
import com.sbaproject.sbamindmap.dto.response.CollectionResponse;
import com.sbaproject.sbamindmap.entity.Collection;
import com.sbaproject.sbamindmap.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {UserService.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CollectionMapper {

    @Mapping(target = "userId", source = "user.id")
    CollectionResponse toResponse(Collection collection);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "userId")
    Collection toEntity(CollectionRequest collectionRequest);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "userId")
    void updateEntityFromRequest(@MappingTarget Collection collection,
                                 CollectionRequest collectionRequest);
}
