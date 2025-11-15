package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.CreateMindmapFromDataRequest;
import com.sbaproject.sbamindmap.dto.request.MindmapRequest;
import com.sbaproject.sbamindmap.dto.response.MindmapDetailResponse;
import com.sbaproject.sbamindmap.dto.response.MindmapResponse;
import com.sbaproject.sbamindmap.entity.Mindmap;

import java.util.List;

public interface MindmapService {
    Mindmap findById(long mindmapId);
    MindmapResponse findResponseById(long mindmapId);
    List<MindmapResponse> findAllMindmaps();
    MindmapResponse savedMindmap(MindmapRequest mindmapRequest);
    MindmapResponse updateMindmap(long mindmapId, MindmapRequest mindmapRequest);
    MindmapResponse deleteMindmap(long mindmapId);

    /**
     * Tạo Mindmap từ GeneratedData
     */
    MindmapResponse createMindmapFromGeneratedData(CreateMindmapFromDataRequest request);

    /**
     * Lấy đầy đủ thông tin mindmap bao gồm nodes, edges, knowledge từ GeneratedData và Template
     * để FE có thể render bằng ReactFlow
     */
    MindmapDetailResponse getMindmapDetail(long mindmapId);
}
