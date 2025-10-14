package com.sbaproject.sbamindmap.mapper;

import com.sbaproject.sbamindmap.dto.request.CategoryRequest;
import com.sbaproject.sbamindmap.dto.response.CategoryResponse;
import com.sbaproject.sbamindmap.entity.Category;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequest categoryRequest);
    CategoryResponse toResponse(Category category);
}
