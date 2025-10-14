package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.CategoryRequest;
import com.sbaproject.sbamindmap.dto.response.CategoryResponse;
import com.sbaproject.sbamindmap.entity.Category;

public interface CategoryService {
    Category findById(long categoryId);
    CategoryResponse findCategoryResponseById(long categoryId);
    CategoryResponse addCategory(CategoryRequest categoryRequest);
}
