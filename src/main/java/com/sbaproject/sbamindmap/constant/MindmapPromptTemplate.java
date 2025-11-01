package com.sbaproject.sbamindmap.constant;

public final class MindmapPromptTemplate {
    public static final String MINDMAP_JSON_INSTRUCTION = """
        You must respond with valid JSON for a mindmap in this exact format:
        {
          "nodes": [
            {"id": "1", "data": {"label": "Root Topic"}, "position": {"x": 0, "y": 0}},
            {"id": "2", "data": {"label": "Subtopic"}, "position": {"x": 200, "y": 100}}
          ],
          "edges": [
            {"id": "e1-2", "source": "1", "target": "2"}
          ]
        }
        No explanation, only JSON.
        """;

    private MindmapPromptTemplate() {
        // prevent instantiation
    }
}

