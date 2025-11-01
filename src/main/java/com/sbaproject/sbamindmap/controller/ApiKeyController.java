package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.entity.ApiKey;
import com.sbaproject.sbamindmap.service.EntitlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keys")
@RequiredArgsConstructor
public class ApiKeyController {
    @Autowired
    private EntitlementService entitlementService;

    // ⚙️ Lấy danh sách API Key hợp lệ
    @GetMapping("/available")
    public List<ApiKey> listAvailableKeys(@RequestParam("uid") Long userId) {
        // (userId lấy từ query param để test, production sẽ dùng JWT)
        return entitlementService.getAvailableKeys(userId);
    }

    // ⚙️ Kiểm tra một key cụ thể có hợp lệ không
    @GetMapping("/{id}/check")
    public boolean checkKey(@RequestParam("uid") Long userId, @PathVariable("id") Long keyId) {
        return entitlementService.isKeyUsable(userId, keyId);
    }
}
