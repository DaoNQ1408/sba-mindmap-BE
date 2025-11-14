package com.sbaproject.sbamindmap.controller.admin;

import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.entity.User;
import com.sbaproject.sbamindmap.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(adminUserService.getAll(), "Success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminUserService.getById(id), "Success"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> create(@RequestBody User user) {
        return ResponseEntity.ok(ApiResponse.success(adminUserService.create(user), "User created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> update(@PathVariable Long id, @RequestBody User request) {
        return ResponseEntity.ok(ApiResponse.success(adminUserService.update(id, request), "User updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        adminUserService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }
}
