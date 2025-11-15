package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.CreateMindmapFromDataRequest;
import com.sbaproject.sbamindmap.dto.request.MindmapRequest;
import com.sbaproject.sbamindmap.dto.response.MindmapDetailResponse;
import com.sbaproject.sbamindmap.dto.response.MindmapResponse;
import com.sbaproject.sbamindmap.dto.response.ResponseBase;
import com.sbaproject.sbamindmap.service.MindmapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mindmaps")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Mindmap Management", description = "APIs for managing mindmaps")
public class MindmapController {

    private final MindmapService mindmapService;

    @GetMapping
    @Operation(summary = "Get all mindmaps", description = "Retrieve all mindmaps")
    public ResponseEntity<ResponseBase> getAllMindmaps() {
        log.info("Fetching all mindmaps");

        List<MindmapResponse> mindmaps = mindmapService.findAllMindmaps();
        return ResponseEntity.ok(new ResponseBase(200, "Mindmaps retrieved successfully", mindmaps));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get mindmap by ID", description = "Retrieve a specific mindmap by ID")
    public ResponseEntity<ResponseBase> getMindmapById(@PathVariable long id) {
        log.info(" Fetching mindmap with ID: {}", id);

        MindmapResponse response = mindmapService.findResponseById(id);
        return ResponseEntity.ok(new ResponseBase(200, "Mindmap retrieved successfully", response));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Get mindmap detail for ReactFlow",
               description = "Retrieve mindmap with full data including nodes, edges, knowledge from GeneratedData and Template for ReactFlow rendering")
    public ResponseEntity<ResponseBase> getMindmapDetail(@PathVariable long id) {
        log.info(" Fetching mindmap detail for ReactFlow with ID: {}", id);

        MindmapDetailResponse response = mindmapService.getMindmapDetail(id);
        return ResponseEntity.ok(new ResponseBase(200, "Mindmap detail retrieved successfully", response));
    }


    @PostMapping
    @Operation(summary = "Create mindmap", description = "Create a new mindmap")
    public ResponseEntity<ResponseBase> createMindmap(@RequestBody MindmapRequest request) {
        log.info("Creating new mindmap: {}", request.getName());

        MindmapResponse savedMindmap = mindmapService.savedMindmap(request);
        return ResponseEntity.ok(new ResponseBase(200, "Mindmap created successfully", savedMindmap));
    }


    @PostMapping("/from-generated-data")
    @Operation(summary = "Create mindmap from generated data",
            description = "Create a new mindmap from AI-generated data")
    public ResponseEntity<ResponseBase> createMindmapFromGeneratedData(
            @RequestBody CreateMindmapFromDataRequest request) {
        log.info(" Creating mindmap from generated data ID: {}", request.getGeneratedDataId());

        MindmapResponse response = mindmapService.createMindmapFromGeneratedData(request);
        return ResponseEntity.ok(new ResponseBase(200, "Mindmap created from generated data successfully", response));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update mindmap", description = "Update an existing mindmap")
    public ResponseEntity<ResponseBase> updateMindmap(
            @PathVariable long id,
            @RequestBody MindmapRequest request) {
        log.info("Updating mindmap with ID: {}", id);

        MindmapResponse updatedMindmap = mindmapService.updateMindmap(id, request);
        return ResponseEntity.ok(new ResponseBase(200, "Mindmap updated successfully", updatedMindmap));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete mindmap", description = "Delete a mindmap by ID")
    public ResponseEntity<ResponseBase> deleteMindmap(@PathVariable long id) {
        log.info(" Deleting mindmap with ID: {}", id);

        MindmapResponse deletedMindmap = mindmapService.deleteMindmap(id);
        return ResponseEntity.ok(new ResponseBase(200, "Mindmap deleted successfully", deletedMindmap));
    }
}
