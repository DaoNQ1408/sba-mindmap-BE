package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.MindmapRequest;
import com.sbaproject.sbamindmap.dto.response.MindmapResponse;
import com.sbaproject.sbamindmap.entity.Mindmap;
import com.sbaproject.sbamindmap.mapper.MindmapMapper;
import com.sbaproject.sbamindmap.repository.MindmapRepository;
import com.sbaproject.sbamindmap.service.MindmapService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MindmapServiceImpl implements MindmapService {

    private final MindmapRepository mindmapRepository;
    private final MindmapMapper mindmapMapper;


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
