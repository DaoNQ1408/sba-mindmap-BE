package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.TemplateRequest;
import com.sbaproject.sbamindmap.dto.response.ResponseBase;
import com.sbaproject.sbamindmap.dto.response.TemplateResponse;
import com.sbaproject.sbamindmap.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Template Management", description = "APIs for managing mindmap templates")
public class TemplateController {

    private final TemplateService templateService;


    @GetMapping
    @Operation(summary = "Get all templates", description = "Retrieve all available templates")
    public ResponseEntity<ResponseBase> getAllTemplates() {
        log.info(" Fetching all templates");

        List<TemplateResponse> templates = templateService.findAllTemplates();
        return ResponseEntity.ok(new ResponseBase(200, "Templates retrieved successfully", templates));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get template by ID", description = "Retrieve a specific template by ID")
    public ResponseEntity<ResponseBase> getTemplateById(@PathVariable long id) {
        log.info("Fetching template with ID: {}", id);

        TemplateResponse response = templateService.findResponseById(id);
        return ResponseEntity.ok(new ResponseBase(200, "Template retrieved successfully", response));
    }


    @PostMapping
    @Operation(summary = "Create template (ADMIN)", description = "Create a new mindmap template")
    public ResponseEntity<ResponseBase> createTemplate(@RequestBody TemplateRequest request) {
        log.info(" Creating new template: {}", request.getName());

        TemplateResponse savedTemplate = templateService.savedTemplate(request);
        return ResponseEntity.ok(new ResponseBase(200, "Template created successfully", savedTemplate));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update template (ADMIN)", description = "Update an existing template")
    public ResponseEntity<ResponseBase> updateTemplate(
            @PathVariable long id,
            @RequestBody TemplateRequest request) {
        log.info("Updating template with ID: {}", id);

        TemplateResponse updatedTemplate = templateService.updateTemplate(id, request);
        return ResponseEntity.ok(new ResponseBase(200, "Template updated successfully", updatedTemplate));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete template (ADMIN)", description = "Delete a template by ID")
    public ResponseEntity<ResponseBase> deleteTemplate(@PathVariable long id) {
        log.info(" Deleting template with ID: {}", id);

        TemplateResponse deletedTemplate = templateService.deleteTemplate(id);
        return ResponseEntity.ok(new ResponseBase(200, "Template deleted successfully", deletedTemplate));
    }
}
