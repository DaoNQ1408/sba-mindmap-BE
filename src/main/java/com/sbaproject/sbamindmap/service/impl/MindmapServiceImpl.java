package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.CreateMindmapFromDataRequest;
import com.sbaproject.sbamindmap.dto.request.MindmapRequest;
import com.sbaproject.sbamindmap.dto.response.MindmapDetailResponse;
import com.sbaproject.sbamindmap.dto.response.MindmapResponse;
import com.sbaproject.sbamindmap.entity.*;
import com.sbaproject.sbamindmap.enums.SharedStatus;
import com.sbaproject.sbamindmap.mapper.MindmapMapper;
import com.sbaproject.sbamindmap.repository.*;
import com.sbaproject.sbamindmap.service.MindmapService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MindmapServiceImpl implements MindmapService {

    private final MindmapRepository mindmapRepository;
    private final MindmapMapper mindmapMapper;
    private final GeneratedDataRepository generatedDataRepository;
    private final TemplateRepository templateRepository;
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;

    @Override
    public Mindmap findById(long mindmapId) {
        return mindmapRepository.findById(mindmapId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Mindmap not found with id: " +
                                        mindmapId)
                );
    }

    @Override
    public MindmapResponse findResponseById(long mindmapId) {
        Mindmap mindmap = findById(mindmapId);
        return mindmapMapper.toResponse(mindmap);
    }

    @Override
    public List<MindmapResponse> findAllMindmaps() {
        return mindmapRepository.findAll().stream()
                .map(mindmapMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public MindmapResponse savedMindmap(MindmapRequest mindmapRequest) {
        validateMindmapNameUniqueness(mindmapRequest.getName());

        Mindmap mindmap = mindmapMapper.toEntity(mindmapRequest);

        Mindmap savedMindmap = mindmapRepository.save(mindmap);

        return mindmapMapper.toResponse(savedMindmap);
    }

    @Override
    @Transactional
    public MindmapResponse createMindmapFromGeneratedData(CreateMindmapFromDataRequest request) {
        log.info("🎨 Creating mindmap from generated data - generatedDataId: {}, templateId: {}",
                request.getGeneratedDataId(), request.getTemplateId());

        // 1. Validate generatedData tồn tại
        GeneratedData generatedData = generatedDataRepository.findById(request.getGeneratedDataId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "GeneratedData not found with ID: " + request.getGeneratedDataId()));

        // 2. Validate template tồn tại
        Template template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Template not found with ID: " + request.getTemplateId()));

        // 3. Lấy user từ conversation của message
        User user = generatedData.getMessage().getConversation().getUser();

        // 4. Xử lý collection
        Collection collection;
        if (request.getCollectionId() != null) {
            // Sử dụng collection có sẵn
            collection = collectionRepository.findById(request.getCollectionId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Collection not found with ID: " + request.getCollectionId()));
        } else {
            // Tạo collection mới
            collection = Collection.builder()
                    .name(request.getName() != null ? request.getName() + " Collection" : "Default Collection")
                    .sharedStatus(request.getSharedStatus() != null ? request.getSharedStatus() : SharedStatus.PRIVATE)
                    .user(user)
                    .build();
            collection = collectionRepository.save(collection);
            log.info("✅ Created new collection with ID: {}", collection.getId());
        }

        // 5. Tạo mindmap
        Mindmap mindmap = Mindmap.builder()
                .name(request.getName() != null ? request.getName() : "Untitled Mindmap")
                .sharedStatus(request.getSharedStatus() != null ? request.getSharedStatus() : SharedStatus.PRIVATE)
                .generatedData(generatedData)
                .collection(collection)
                .template(template)
                .build();

        Mindmap savedMindmap = mindmapRepository.save(mindmap);

        log.info("✅ Mindmap created successfully with ID: {}", savedMindmap.getId());

        return mindmapMapper.toResponse(savedMindmap);
    }

    @Override
    @Transactional
    public MindmapResponse updateMindmap(long mindmapId, MindmapRequest mindmapRequest) {
        Mindmap mindmap = findById(mindmapId);

        mindmapMapper.updateEntityFromRequest(mindmap, mindmapRequest);

        Mindmap updatedMindmap = mindmapRepository.save(mindmap);

        return mindmapMapper.toResponse(updatedMindmap);
    }

    @Override
    @Transactional
    public MindmapResponse deleteMindmap(long mindmapId) {
        Mindmap mindmap = findById(mindmapId);

        mindmapRepository.delete(mindmap);

        return mindmapMapper.toResponse(mindmap);
    }

    @Override
    public MindmapDetailResponse getMindmapDetail(long mindmapId) {
        log.info("🎨 Fetching mindmap detail for ReactFlow - mindmapId: {}", mindmapId);

        // Lấy mindmap
        Mindmap mindmap = findById(mindmapId);

        // Lấy generated data
        GeneratedData generatedData = mindmap.getGeneratedData();
        if (generatedData == null) {
            throw new EntityNotFoundException("GeneratedData not found for mindmap ID: " + mindmapId);
        }

        // Lấy template
        Template template = mindmap.getTemplate();
        if (template == null) {
            throw new EntityNotFoundException("Template not found for mindmap ID: " + mindmapId);
        }

        // Lấy collection
        Collection collection = mindmap.getCollection();

        // Build response
        MindmapDetailResponse response = MindmapDetailResponse.builder()
                .mindmapId(mindmap.getId())
                .name(mindmap.getName())
                .sharedStatus(mindmap.getSharedStatus())
                .createdAt(mindmap.getCreatedAt())
                .updatedAt(mindmap.getUpdatedAt())
                // Data từ GeneratedData
                .nodes(generatedData.getNodes())
                .edges(generatedData.getEdges())
                .knowledgeJson(generatedData.getKnowledgeJson())
                // Data từ Template
                .templateId(template.getId())
                .templateName(template.getName())
                .templateDescription(null)  // Template không có field description
                // Data từ Collection
                .collectionId(collection != null ? collection.getId() : null)
                .collectionName(collection != null ? collection.getName() : null)
                .build();

        log.info("✅ Mindmap detail retrieved successfully with {} nodes",
                generatedData.getNodes() != null ? "valid" : "null");

        return response;
    }

    public void validateMindmapNameUniqueness(String name) {
        mindmapRepository.findByName(name)
                .ifPresent(mindmap -> {
                    throw new RuntimeException(
                            "Mindmap " +
                                    name +
                                    " already exists");
                });
    }
}
