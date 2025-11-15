package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.response.GeneratedDataResponse;
import com.sbaproject.sbamindmap.dto.response.ResponseBase;
import com.sbaproject.sbamindmap.service.GeneratedDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generated-data")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Generated Data Management", description = "APIs for managing AI-generated mindmap data")
public class GeneratedDataController {

    private final GeneratedDataService generatedDataService;

    /**
     * Lấy generated data theo ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get generated data by ID", description = "Retrieve a specific generated data by ID")
    public ResponseEntity<ResponseBase> getGeneratedData(@PathVariable Long id) {
        log.info(" Fetching generated data with ID: {}", id);

        GeneratedDataResponse response = generatedDataService.getGeneratedDataById(id);
        return ResponseEntity.ok(new ResponseBase(200, "Generated data retrieved successfully", response));
    }

    /**
     * Lấy tất cả generated data
     */
    @GetMapping
    @Operation(summary = "Get all generated data", description = "Retrieve all generated mindmap data")
    public ResponseEntity<ResponseBase> getAllGeneratedData() {
        log.info(" Fetching all generated data");

        List<GeneratedDataResponse> responses = generatedDataService.getAllGeneratedData();
        return ResponseEntity.ok(new ResponseBase(200, "Generated data list retrieved successfully", responses));
    }

    /**
     * Đánh dấu generated data đã được kiểm tra
     */
    @PatchMapping("/{id}/check")
    @Operation(summary = "Mark as checked", description = "Mark a generated data as checked/reviewed")
    public ResponseEntity<ResponseBase> markAsChecked(@PathVariable Long id) {
        log.info("Marking generated data as checked: {}", id);

        GeneratedDataResponse response = generatedDataService.markAsChecked(id);
        return ResponseEntity.ok(new ResponseBase(200, "Generated data marked as checked successfully", response));
    }

    /**
     * Xóa generated data
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete generated data", description = "Delete a generated data by ID")
    public ResponseEntity<ResponseBase> deleteGeneratedData(@PathVariable Long id) {
        log.info(" Deleting generated data with ID: {}", id);

        generatedDataService.deleteGeneratedData(id);
        return ResponseEntity.ok(new ResponseBase(200, "Generated data deleted successfully", null));
    }
}
