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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    Collection toEntity(CollectionRequest collectionRequest);


    CollectionResponse toResponse(Collection collection);


    void updateEntityFromRequest(@MappingTarget Collection collection,
                                 CollectionRequest collectionRequest);
}
