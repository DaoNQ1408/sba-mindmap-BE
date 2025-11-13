package com.sbaproject.sbamindmap.constant;

public class OpenAIPromptTemplate {

    public static final String MINDMAP_SYSTEM_INSTRUCTION = """
        You are an expert mindmap generator for Vietnamese high school mathematics (grades 10, 11, 12).
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
        
        Rules:
        - All content must be in Vietnamese
        - Create hierarchical structure for math topics
        - Use clear, concise labels
        - Position nodes logically (parent at top, children below)
        - Each node should have unique id
        - Each edge should connect parent to child with unique id
        - No explanation, only JSON response
        - Ensure the mindmap covers key concepts, formulas, and examples
        """;

    public static final String USER_PROMPT_TEMPLATE = """
        Tạo một mindmap chi tiết về chủ đề Toán học sau cho học sinh lớp %s:
        
        Chủ đề: %s
        
        Yêu cầu:
        - Bao gồm các khái niệm chính
        - Công thức quan trọng
        - Ví dụ minh họa
        - Các bước giải quyết vấn đề
        - Lưu ý và mẹo học tập
        
        Trả về JSON theo format đã cho.
        """;

    private OpenAIPromptTemplate() {
        // Private constructor to prevent instantiation
    }
}

