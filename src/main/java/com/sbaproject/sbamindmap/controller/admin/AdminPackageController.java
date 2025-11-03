package com.sbaproject.sbamindmap.controller.admin;

import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.entity.Packages;
import com.sbaproject.sbamindmap.service.admin.AdminPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/packages")
@RequiredArgsConstructor
public class AdminPackageController {

    private final AdminPackageService adminPackageService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Packages>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(adminPackageService.getAll(), "Success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Packages>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminPackageService.getById(id), "Success"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Packages>> create(@RequestBody Packages pkg) {
        return ResponseEntity.ok(ApiResponse.success(adminPackageService.create(pkg), "Package created"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Packages>> update(@PathVariable Long id, @RequestBody Packages pkg) {
        return ResponseEntity.ok(ApiResponse.success(adminPackageService.update(id, pkg), "Package updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        adminPackageService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Package deleted successfully"));
    }
}
