package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.dto.response.CategoryResponse;
import com.sbaproject.sbamindmap.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable(value = "id") long categoryId) {
        CategoryResponse response = categoryService.findCategoryResponseById(categoryId);
        return ResponseEntity.ok(ApiResponse.success(response, "Retrieved category with id: " + categoryId));
    }
}
