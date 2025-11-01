package com.sbaproject.sbamindmap.enums;

import com.sbaproject.sbamindmap.constant.MindmapPromptTemplate;

public enum MindmapTemplateType {
    BASIC(MindmapPromptTemplate.MINDMAP_JSON_INSTRUCTION);

    private final String instruction;

    MindmapTemplateType(String instruction) {
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }
}

