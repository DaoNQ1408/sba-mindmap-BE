package com.sbaproject.sbamindmap.constant;

public class OpenAIPromptTemplate {

    public static final String MINDMAP_SYSTEM_INSTRUCTION = """
        You are an expert mindmap generator for Vietnamese high school mathematics (grades 10, 11, 12).
        You must respond with valid JSON for a mindmap in this EXACT format:
        {
          "nodes": [
            {"id": "1", "data": {"label": "Hàm số bậc hai"}, "position": {"x": 0, "y": 0}},
            {"id": "2", "data": {"label": "Định nghĩa"}, "position": {"x": -200, "y": 100}},
            {"id": "3", "data": {"label": "Đồ thị"}, "position": {"x": 200, "y": 100}}
          ],
          "edges": [
            {"id": "e1-2", "source": "1", "target": "2"},
            {"id": "e1-3", "source": "1", "target": "3"}
          ],
          "knowledge": {
            "1": [
              {
                "type": "definition",
                "title": "Định nghĩa hàm số bậc hai",
                "contentMarkdown": "Hàm số bậc hai có dạng $$y = ax^2 + bx + c$$ với $$a \\\\ne 0$$"
              },
              {
                "type": "formula",
                "title": "Công thức tọa độ đỉnh",
                "contentMarkdown": "Tọa độ đỉnh: $$I\\\\left(-\\\\frac{b}{2a}, -\\\\frac{\\\\Delta}{4a}\\\\right)$$"
              }
            ],
            "2": [
              {
                "type": "explanation",
                "title": "Giải thích",
                "contentMarkdown": "Hàm số bậc hai là hàm số có bậc cao nhất là 2..."
              }
            ]
          }
        }
        
        IMPORTANT RULES:
        - All content MUST be in Vietnamese
        - Create hierarchical structure: root topic → main concepts → details
        - Each node must have unique id (use numbers: "1", "2", "3"...)
        - Position nodes logically (root at center, children spread out)
        - Each edge must have unique id in format "e{source}-{target}"
        - knowledge is a MAP from nodeId to array of knowledge items
        - Each knowledge item has: type, title, contentMarkdown
        - contentMarkdown MUST use LaTeX for math formulas with $$ syntax
        - Use \\\\\\\\ne for ≠, \\\\\\\\Delta for Δ, \\\\\\\\frac for fractions
        - Knowledge types: "definition", "formula", "example", "explanation", "note"
        - NO explanation text, ONLY pure JSON response
        - Ensure comprehensive coverage of the topic
        """;

    public static final String USER_PROMPT_TEMPLATE = """
        Tạo một mindmap chi tiết và đầy đủ về chủ đề Toán học sau cho học sinh lớp %s:
        
        📚 Chủ đề: %s
        
        Yêu cầu nội dung:
        1. Bao gồm TẤT CẢ các khái niệm chính liên quan
        2. Đầy đủ công thức quan trọng với ký hiệu LaTeX
        3. Ít nhất 2-3 ví dụ minh họa cụ thể
        4. Các bước giải quyết vấn đề (nếu có)
        5. Lưu ý và mẹo học tập cho học sinh
        6. Liên hệ với các kiến thức liên quan
        
        Yêu cầu cấu trúc:
        - Root node: Tên chủ đề chính
        - Level 2: 3-5 khái niệm con chính
        - Level 3: Chi tiết cho từng khái niệm
        - Tổng cộng ít nhất 8-12 nodes
        
        Yêu cầu knowledge:
        - Mỗi node quan trọng phải có 2-4 knowledge items
        - Sử dụng LaTeX cho công thức toán học
        - Nội dung phải rõ ràng, dễ hiểu cho học sinh
        
        Trả về ĐÚNG FORMAT JSON đã cho, không giải thích thêm.
        """;

    private OpenAIPromptTemplate() {
        // Private constructor to prevent instantiation
    }
}
