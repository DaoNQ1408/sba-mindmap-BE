package com.sbaproject.sbamindmap.mapper;

import com.sbaproject.sbamindmap.dto.request.TemplateRequest;
import com.sbaproject.sbamindmap.dto.response.TemplateResponse;
import com.sbaproject.sbamindmap.entity.Template;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateMapper {

    TemplateResponse toResponse(Template template);


    @Mapping(target = "id", ignore = true)
    Template toEntity(TemplateRequest templateRequest);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(@MappingTarget Template template,
                                 TemplateRequest templateRequest);

}
