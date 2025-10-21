package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.MindmapRequest;
import com.sbaproject.sbamindmap.dto.response.MindmapResponse;
import com.sbaproject.sbamindmap.service.MindmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mindmaps")
@RequiredArgsConstructor
public class MindmapController {

    private final MindmapService mindmapService;


    @GetMapping
    public ResponseEntity<List<MindmapResponse>> getAllMindmaps() {
        List<MindmapResponse> mindmaps = mindmapService.findAllMindmaps();
        return ResponseEntity.ok(mindmaps);
    }


    @GetMapping("/{id}")
    public ResponseEntity<MindmapResponse> getMindmapById(@PathVariable long id) {
        MindmapResponse response = mindmapService.findResponseById(id);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<MindmapResponse> createMindmap(@RequestBody MindmapRequest request) {
        MindmapResponse savedMindmap = mindmapService.savedMindmap(request);
        return ResponseEntity.ok(savedMindmap);
    }


    @PutMapping("/{id}")
    public ResponseEntity<MindmapResponse> updateMindmap(@PathVariable long id,
                                                         @RequestBody MindmapRequest request) {
        MindmapResponse updatedMindmap = mindmapService.updateMindmap(id, request);
        return ResponseEntity.ok(updatedMindmap);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<MindmapResponse> deleteMindmap(@PathVariable long id) {
        MindmapResponse deletedMindmap = mindmapService.deleteMindmap(id);
        return ResponseEntity.ok(deletedMindmap);
    }
}
