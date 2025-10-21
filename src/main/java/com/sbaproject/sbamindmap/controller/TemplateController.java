package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.TemplateRequest;
import com.sbaproject.sbamindmap.dto.response.TemplateResponse;
import com.sbaproject.sbamindmap.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;


    @GetMapping
    public ResponseEntity<List<TemplateResponse>> getAllTemplates() {
        List<TemplateResponse> templates = templateService.findAllTemplates();
        return ResponseEntity.ok(templates);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TemplateResponse> getTemplateById(@PathVariable long id) {
        TemplateResponse response = templateService.findResponseById(id);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<TemplateResponse> createTemplate(@RequestBody TemplateRequest request) {
        TemplateResponse savedTemplate = templateService.savedTemplate(request);
        return ResponseEntity.ok(savedTemplate);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TemplateResponse> updateTemplate(
            @PathVariable long id,
            @RequestBody TemplateRequest request) {
        TemplateResponse updatedTemplate = templateService.updateTemplate(id, request);
        return ResponseEntity.ok(updatedTemplate);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<TemplateResponse> deleteTemplate(@PathVariable long id) {
        TemplateResponse deletedTemplate = templateService.deleteTemplate(id);
        return ResponseEntity.ok(deletedTemplate);
    }
}
