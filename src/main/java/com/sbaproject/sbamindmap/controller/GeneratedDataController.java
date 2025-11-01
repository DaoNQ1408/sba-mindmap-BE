package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.entity.GeneratedData;
import com.sbaproject.sbamindmap.repository.GeneratedDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generated-data")
@RequiredArgsConstructor
public class GeneratedDataController {

    private final GeneratedDataRepository generatedDataRepository;

    /**
     * Lấy generated data theo ID
     */
    @GetMapping("/{id}")
    public GeneratedData getGeneratedData(@PathVariable Long id) {
        return generatedDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GeneratedData not found: " + id));
    }

    /**
     * Lấy tất cả generated data
     */
    @GetMapping
    public List<GeneratedData> getAllGeneratedData() {
        return generatedDataRepository.findAll();
    }

    /**
     * Đánh dấu generated data đã được kiểm tra
     */
    @PatchMapping("/{id}/check")
    public GeneratedData markAsChecked(@PathVariable Long id) {
        GeneratedData data = generatedDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GeneratedData not found: " + id));
        data.setIsChecked(true);
        return generatedDataRepository.save(data);
    }

    /**
     * Xóa generated data
     */
    @DeleteMapping("/{id}")
    public void deleteGeneratedData(@PathVariable Long id) {
        generatedDataRepository.deleteById(id);
    }
}

