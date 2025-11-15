package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.response.GeneratedDataResponse;
import com.sbaproject.sbamindmap.entity.GeneratedData;
import com.sbaproject.sbamindmap.exception.ResourceNotFoundException;
import com.sbaproject.sbamindmap.repository.GeneratedDataRepository;
import com.sbaproject.sbamindmap.service.GeneratedDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneratedDataServiceImpl implements GeneratedDataService {

    private final GeneratedDataRepository generatedDataRepository;

    @Override
    public GeneratedDataResponse getGeneratedDataById(Long id) {
        GeneratedData data = generatedDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GeneratedData not found with id: " + id));
        return convertToResponse(data);
    }

    @Override
    public List<GeneratedDataResponse> getAllGeneratedData() {
        return generatedDataRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GeneratedDataResponse markAsChecked(Long id) {
        GeneratedData data = generatedDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GeneratedData not found with id: " + id));
        data.setIsChecked(true);
        GeneratedData saved = generatedDataRepository.save(data);
        return convertToResponse(saved);
    }

    @Override
    @Transactional
    public void deleteGeneratedData(Long id) {
        if (!generatedDataRepository.existsById(id)) {
            throw new ResourceNotFoundException("GeneratedData not found with id: " + id);
        }
        generatedDataRepository.deleteById(id);
    }

    /**
     * Convert Entity sang DTO để tránh Hibernate lazy loading serialization error
     */
    private GeneratedDataResponse convertToResponse(GeneratedData data) {
        return GeneratedDataResponse.builder()
                .generatedDataId(data.getGeneratedDataId())
                .nodes(data.getNodes())
                .edges(data.getEdges())
                .knowledgeJson(data.getKnowledgeJson())
                .isChecked(data.getIsChecked())
                .createdAt(data.getCreatedAt())
                .build();
    }
}
