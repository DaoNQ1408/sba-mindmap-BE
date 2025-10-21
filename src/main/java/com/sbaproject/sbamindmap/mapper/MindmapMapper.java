package com.sbaproject.sbamindmap.mapper;

import com.sbaproject.sbamindmap.dto.request.MindmapRequest;
import com.sbaproject.sbamindmap.dto.response.MindmapResponse;
import com.sbaproject.sbamindmap.entity.Mindmap;
import com.sbaproject.sbamindmap.service.CollectionService;
import com.sbaproject.sbamindmap.service.TemplateService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {TemplateService.class,
                CollectionService.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MindmapMapper {

    @Mapping(target = "templateId", source = "template.id")
    @Mapping(target = "collectionId", source = "collection.id")
    MindmapResponse toResponse(Mindmap mindmap);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "template", source = "templateId")
    @Mapping(target = "collection", source = "collectionId")
    Mindmap toEntity(MindmapRequest mindmapRequest);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "template", source = "templateId")
    @Mapping(target = "collection", source = "collectionId")
    void updateEntityFromRequest(@MappingTarget Mindmap mindmap,
                                 MindmapRequest mindmapRequest);
}
