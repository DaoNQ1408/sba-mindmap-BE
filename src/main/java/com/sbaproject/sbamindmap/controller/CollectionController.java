package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.CollectionRequest;
import com.sbaproject.sbamindmap.dto.response.CollectionResponse;
import com.sbaproject.sbamindmap.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;


    @GetMapping
    public ResponseEntity<List<CollectionResponse>> getAllCollections() {
        List<CollectionResponse> collections = collectionService.findAllCollections();
        return ResponseEntity.ok(collections);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CollectionResponse> getCollectionById(@PathVariable("id") long id) {
        CollectionResponse response = collectionService.findResponseById(id);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<CollectionResponse> createCollection(@RequestBody CollectionRequest request) {
        CollectionResponse created = collectionService.savedCollection(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CollectionResponse> updateCollection(@PathVariable("id") long id,
                                                               @RequestBody CollectionRequest request) {

        CollectionResponse updated = collectionService.updateCollection(id, request);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<CollectionResponse> deleteCollection(@PathVariable("id") long id) {
        CollectionResponse deleted = collectionService.deleteCollection(id);
        return ResponseEntity.ok(deleted);
    }
}
