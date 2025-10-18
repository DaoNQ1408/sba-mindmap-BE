package com.sbaproject.sbamindmap.mapper;

import com.sbaproject.sbamindmap.dto.request.CategoryRequest;
import com.sbaproject.sbamindmap.dto.response.CategoryResponse;
import com.sbaproject.sbamindmap.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);


    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequest categoryRequest);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(@MappingTarget Category category,
                                 CategoryRequest categoryRequest);
}
