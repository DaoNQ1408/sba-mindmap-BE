package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.MindmapRequest;
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
}
