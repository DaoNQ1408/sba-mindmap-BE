package com.sbaproject.sbamindmap.mapper;

import com.sbaproject.sbamindmap.dto.request.CategoryRequest;
import com.sbaproject.sbamindmap.dto.response.CategoryResponse;
import com.sbaproject.sbamindmap.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);


    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequest categoryRequest);


    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(@MappingTarget Category category,
                                 CategoryRequest categoryRequest);
}
