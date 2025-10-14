package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.CategoryRequest;
import com.sbaproject.sbamindmap.dto.response.CategoryResponse;
import com.sbaproject.sbamindmap.entity.Category;
import com.sbaproject.sbamindmap.exception.DuplicateObjectException;
import com.sbaproject.sbamindmap.mapper.CategoryMapper;
import com.sbaproject.sbamindmap.repository.CategoryRepository;
import com.sbaproject.sbamindmap.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Override
    @Transactional
    public Category findById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Category not found with id " +
                                categoryId)
                );
    }

    @Override
    public CategoryResponse findCategoryResponseById(long categoryId) {
        return categoryMapper.toResponse(findById(categoryId));
    }

    @Override
    @Transactional
    public CategoryResponse addCategory(CategoryRequest categoryRequest) {

        endIfCategoryNameExists(categoryRequest.getName());

        Category category = categoryMapper.toEntity(categoryRequest);

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    public void endIfCategoryNameExists(String categoryName) {
        categoryRepository.findByName(categoryName)
                .ifPresent(category -> {
                    throw new DuplicateObjectException(
                            "Category " +
                                    categoryName +
                                    " already exists");
                });
    }
}
