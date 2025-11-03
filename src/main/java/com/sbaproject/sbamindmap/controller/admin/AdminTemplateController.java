package com.sbaproject.sbamindmap.controller.admin;

import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.entity.Template;
import com.sbaproject.sbamindmap.service.admin.AdminTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/templates")
@RequiredArgsConstructor
public class AdminTemplateController {

    private final AdminTemplateService adminTemplateService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Template>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(adminTemplateService.getAll(), "Success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Template>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminTemplateService.getById(id), "Success"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Template>> create(@RequestBody Template template) {
        return ResponseEntity.ok(ApiResponse.success(adminTemplateService.create(template), "Template created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Template>> update(@PathVariable Long id, @RequestBody Template request) {
        return ResponseEntity.ok(ApiResponse.success(adminTemplateService.update(id, request), "Template updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        adminTemplateService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Template deleted successfully"));
    }
}
