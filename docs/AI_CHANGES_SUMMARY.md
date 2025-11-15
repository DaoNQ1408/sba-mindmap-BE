# 📋 TÓM TẮT THAY ĐỔI HỆ THỐNG - AI MINDMAP GENERATOR

**Ngày tạo**: 14/11/2025  
**Dự án**: SBA Mindmap - Backend Spring Boot  
**Mục đích**: Tạo mindmap tự động cho môn Toán THPT (lớp 10, 11, 12) bằng OpenAI

---

## 🎯 MỤC TIÊU ĐÃ HOÀN THÀNH

✅ **Security mở hoàn toàn** - Không cần JWT token cho test  
✅ **Validate chủ đề Toán 10-11-12** - Từ chối topic ngoài phạm vi  
✅ **Gọi OpenAI từ API key trong DB** - Lấy từ bảng `api_keys`  
✅ **Parse JSON với knowledge** - Hỗ trợ nodes, edges, knowledge  
✅ **Lưu lịch sử chat** - Đầy đủ conversation, message, generated_data  
✅ **Trả response bằng ResponseBase** - Chuẩn format `{code, message, data}`  
✅ **Tạo Mindmap từ GeneratedData** - Endpoint mới  
✅ **Giảm remaining_calls** - Tự động trừ mỗi lần gọi AI  

---

## 📁 CÁC FILE ĐÃ THAY ĐỔI

### 1. **SecurityConfig.java** ⚙️
**Đường dẫn**: `src/main/java/com/sbaproject/sbamindmap/config/SecurityConfig.java`

**Thay đổi**:
- ✅ Tắt hoàn toàn JWT authentication
- ✅ `.anyRequest().permitAll()` - Cho phép tất cả request
- ✅ Không có filter JWT nào được áp dụng

**Lý do**:
- Để test qua Swagger và Postman không cần token
- Giai đoạn development - chưa cần bảo mật

```java
.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
```

---

### 2. **MathTopicValidator.java** 🔍 (FILE MỚI)
**Đường dẫn**: `src/main/java/com/sbaproject/sbamindmap/util/MathTopicValidator.java`

**Chức năng**:
- ✅ Kiểm tra `grade` hợp lệ (10, 11, 12)
- ✅ Kiểm tra `topic` có chứa từ khóa Toán không
- ✅ Danh sách 40+ từ khóa Toán THPT

**Danh sách từ khóa**:
```
hàm số, phương trình, bất phương trình, lượng giác, 
đạo hàm, tích phân, vectơ, logarit, số phức, xác suất, ...
```

**Validation Result**:
```java
ValidationResult result = validator.validate("Hàm số bậc hai", "10");
if (!result.isValid()) {
    // Trả về message từ chối
}
```

---

### 3. **OpenAIPromptTemplate.java** 📝
**Đường dẫn**: `src/main/java/com/sbaproject/sbamindmap/constant/OpenAIPromptTemplate.java`

**Thay đổi**:
- ✅ Thêm `knowledge` vào format JSON yêu cầu
- ✅ Hỗ trợ LaTeX cho công thức toán ($$ syntax)
- ✅ Yêu cầu AI tạo 8-12 nodes với kiến thức chi tiết

**Format JSON mới**:
```json
{
  "nodes": [...],
  "edges": [...],
  "knowledge": {
    "1": [
      {
        "type": "definition",
        "title": "Định nghĩa",
        "contentMarkdown": "$$y = ax^2 + bx + c$$"
      }
    ]
  }
}
```

---

### 4. **ChatServiceImpl.java** 🤖
**Đường dẫn**: `src/main/java/com/sbaproject/sbamindmap/service/impl/ChatServiceImpl.java`

**Thay đổi lớn**:

#### A. **generateMindmap()** - Tạo mindmap từ topic
```java
1. Validate topic & grade bằng MathTopicValidator
2. Nếu KHÔNG hợp lệ:
   - Lưu message user
   - Lưu message assistant (từ chối)
   - KHÔNG gọi AI
   - KHÔNG trừ remaining_calls
   - Trả về response với message từ chối

3. Nếu HỢP LỆ:
   - Lấy API key từ DB (findAvailableKeys)
   - Gọi OpenAI với API key đó
   - Parse JSON (nodes, edges, knowledge)
   - Lưu GeneratedData với knowledgeJson
   - Giảm remaining_calls
   - Trả về response với data đầy đủ
```

#### B. **chat()** - Chat thông thường
```java
1. Validate topic (mặc định grade=10)
2. Nếu KHÔNG hợp lệ -> từ chối
3. Nếu hợp lệ -> gọi AI & trừ remaining_calls
```

#### C. **callOpenAI()** - Gọi OpenAI API
```java
// Tạo OpenAI service với API key từ DB
OpenAiService openAiService = new OpenAiService(apiKeyValue, Duration.ofSeconds(60));

// Lấy lịch sử (10 tin nhắn gần nhất)
// Thêm system instruction
// Thêm user message
// Gọi API
// Shutdown executor
```

#### D. **validateAndParseMindmapJson()** - Parse JSON
```java
// Làm sạch markdown code blocks (```json)
// Parse JSON
// Tách nodes, edges, knowledge thành 3 string riêng
// Validate structure
// Trả về MindmapJsonData
```

**Dependency injection**:
```java
private final MathTopicValidator mathTopicValidator;
private final ApiKeyRepository apiKeyRepository;
```

---

### 5. **ChatController.java** 🎮
**Đường dẫn**: `src/main/java/com/sbaproject/sbamindmap/controller/ChatController.java`

**Thay đổi**:
- ✅ Sử dụng `ResponseBase` thay vì `ApiResponse`
- ✅ Thêm endpoint `POST /api/v1/chat/conversation`

**Endpoints**:
```
POST   /api/v1/chat/generate-mindmap     → Tạo mindmap tự động
POST   /api/v1/chat/message              → Chat thông thường
GET    /api/v1/chat/conversation/{id}/history → Lịch sử chat
GET    /api/v1/chat/conversations?userId=1    → Danh sách conversation
POST   /api/v1/chat/conversation?userId=1&title=... → Tạo conversation mới
```

**Response format**:
```json
{
  "code": 200,
  "message": "Mindmap generated successfully",
  "data": { ... }
}
```

---

### 6. **MindmapDataResponse.java** 📊
**Đường dẫn**: `src/main/java/com/sbaproject/sbamindmap/dto/response/MindmapDataResponse.java`

**Thay đổi**:
- ✅ Thêm field `knowledgeJson` (String)

```java
private String knowledgeJson; // JSON string của knowledge MAP
```

---

### 7. **CreateMindmapFromDataRequest.java** 📝 (FILE MỚI)
**Đường dẫn**: `src/main/java/com/sbaproject/sbamindmap/dto/request/CreateMindmapFromDataRequest.java`

**Chức năng**: Request để tạo Mindmap thủ công từ GeneratedData

```java
{
  "generatedDataId": 30,
  "templateId": 3,
  "name": "Mindmap Hàm số bậc hai",
  "sharedStatus": "PRIVATE",
  "collectionId": null  // null = tạo collection mới
}
```

---

### 8. **MindmapController.java** 🗺️
**Đường dẫn**: `src/main/java/com/sbaproject/sbamindmap/controller/MindmapController.java`

**Thay đổi**:
- ✅ Sử dụng `ResponseBase`
- ✅ Thêm endpoint `POST /api/v1/mindmaps/from-generated-data`

**Endpoints**:
```
GET    /api/v1/mindmaps              → List tất cả mindmaps
GET    /api/v1/mindmaps/{id}         → Get mindmap by ID
POST   /api/v1/mindmaps              → Tạo mindmap thông thường
POST   /api/v1/mindmaps/from-generated-data → Tạo từ GeneratedData ⭐
PUT    /api/v1/mindmaps/{id}         → Update mindmap
DELETE /api/v1/mindmaps/{id}         → Delete mindmap
```

---

### 9. **MindmapService.java** & **MindmapServiceImpl.java** 🛠️
**Đường dẫn**: 
- `src/main/java/com/sbaproject/sbamindmap/service/MindmapService.java`
- `src/main/java/com/sbaproject/sbamindmap/service/impl/MindmapServiceImpl.java`

**Thêm method mới**:
```java
MindmapResponse createMindmapFromGeneratedData(CreateMindmapFromDataRequest request);
```

**Logic implementation**:
```java
1. Validate generatedData tồn tại
2. Validate template tồn tại
3. Lấy user từ conversation của message
4. Nếu collectionId null → tạo collection mới
5. Tạo Mindmap entity
6. Save & return response
```

---

## 🔄 LUỒNG HOẠT ĐỘNG HOÀN CHỈNH

### **Luồng 1: Tạo Mindmap Tự Động** 🚀

```
1. FE gửi POST /api/v1/chat/generate-mindmap
   Body: {
     "topic": "Hàm số bậc hai",
     "grade": "10",
     "userId": "1",
     "conversationId": null,
     "templateId": 3
   }

2. ChatController.generateMindmap()
   ↓
3. ChatServiceImpl.generateMindmap()
   ↓
4. MathTopicValidator.validate()
   - Kiểm tra grade (10/11/12)
   - Kiểm tra topic có từ khóa Toán
   ↓
5a. NẾU KHÔNG HỢP LỆ:
   - Lưu message user
   - Lưu message assistant (từ chối)
   - Trả về ResponseBase với message từ chối
   - KHÔNG gọi AI, KHÔNG trừ calls
   ↓
5b. NẾU HỢP LỆ:
   - Lấy API key từ DB (api_keys table)
   - Gọi OpenAI với key đó
   - Nhận JSON response (nodes, edges, knowledge)
   - Parse JSON
   - Lưu message user
   - Lưu message assistant
   - Lưu GeneratedData:
     * nodes (JSON string)
     * edges (JSON string)
     * knowledgeJson (JSON string)
     * isChecked = false
   - Giảm remaining_calls của API key
   - Trả về ResponseBase với data đầy đủ
```

### **Luồng 2: Tạo Mindmap Thủ Công từ GeneratedData** 🎨

```
1. FE gửi POST /api/v1/mindmaps/from-generated-data
   Body: {
     "generatedDataId": 30,
     "templateId": 3,
     "name": "Mindmap Hàm số",
     "sharedStatus": "PRIVATE",
     "collectionId": null
   }

2. MindmapController.createMindmapFromGeneratedData()
   ↓
3. MindmapServiceImpl.createMindmapFromGeneratedData()
   - Validate generatedData tồn tại
   - Validate template tồn tại
   - Lấy user từ generatedData.message.conversation.user
   - Nếu collectionId = null → tạo collection mới
   - Tạo Mindmap entity
   - Save & return MindmapResponse
```

---

## 📊 DATABASE SCHEMA

### Bảng: `generated_datas`
```sql
generated_data_id    BIGINT PRIMARY KEY
message_id           BIGINT (FK → messages)
nodes                TEXT (JSON string)
edges                TEXT (JSON string)
knowledge_json       JSONB (MAP từ nodeId → knowledge items)
is_checked           BOOLEAN
created_at           TIMESTAMP
```

**Ví dụ dữ liệu**:
```json
{
  "nodes": "[{\"id\":\"1\",\"data\":{\"label\":\"Hàm số bậc hai\"},...}]",
  "edges": "[{\"id\":\"e1-2\",\"source\":\"1\",\"target\":\"2\"}]",
  "knowledge_json": {
    "1": [
      {
        "type": "definition",
        "title": "Định nghĩa",
        "contentMarkdown": "$$y = ax^2 + bx + c$$"
      }
    ]
  }
}
```

---

## 🧪 HƯỚNG DẪN TEST QUA POSTMAN

### **Test 1: Tạo Conversation Mới**
```
POST http://localhost:8080/api/v1/chat/conversation?userId=1&title=Test Conversation
Headers: (không cần)
Body: (không cần)

Response:
{
  "code": 200,
  "message": "Conversation created successfully",
  "data": {
    "conversationId": 1,
    "title": "Test Conversation",
    "isActive": true,
    "messageCount": 0
  }
}
```

---

### **Test 2: Tạo Mindmap - HỢP LỆ** ✅
```
POST http://localhost:8080/api/v1/chat/generate-mindmap
Headers: Content-Type: application/json
Body:
{
  "topic": "Hàm số bậc hai",
  "grade": "10",
  "userId": "1",
  "conversationId": 1,
  "templateId": 1
}

Response thành công:
{
  "code": 200,
  "message": "Mindmap generated successfully",
  "data": {
    "messageId": 2,
    "conversationId": 1,
    "role": "assistant",
    "content": "{\"nodes\":[...],\"edges\":[...],\"knowledge\":{...}}",
    "generatedData": {
      "generatedDataId": 1,
      "nodesJson": "[{\"id\":\"1\",...}]",
      "edgesJson": "[{\"id\":\"e1-2\",...}]",
      "knowledgeJson": "{\"1\":[{...}]}",
      "isChecked": false
    }
  }
}
```

---

### **Test 3: Tạo Mindmap - KHÔNG HỢP LỆ** ❌
```
POST http://localhost:8080/api/v1/chat/generate-mindmap
Body:
{
  "topic": "Lịch sử Việt Nam",
  "grade": "10",
  "userId": "1",
  "conversationId": 1
}

Response từ chối:
{
  "code": 200,
  "message": "Mindmap generated successfully",
  "data": {
    "messageId": 3,
    "role": "assistant",
    "content": "Hệ thống chỉ hỗ trợ kiến thức Toán THPT lớp 10-11-12. Vui lòng hỏi lại trong phạm vi này.",
    "generatedData": null
  }
}
```

**Lưu ý**: 
- Vẫn lưu message vào DB
- KHÔNG gọi OpenAI
- KHÔNG trừ remaining_calls

---

### **Test 4: Lấy Lịch Sử Chat**
```
GET http://localhost:8080/api/v1/chat/conversation/1/history

Response:
{
  "code": 200,
  "message": "Conversation history retrieved successfully",
  "data": [
    {
      "messageId": 1,
      "role": "user",
      "content": "Tạo mindmap về hàm số bậc hai...",
      "generatedData": null
    },
    {
      "messageId": 2,
      "role": "assistant",
      "content": "{...JSON...}",
      "generatedData": { ... }
    }
  ]
}
```

---

### **Test 5: Tạo Mindmap Từ GeneratedData**
```
POST http://localhost:8080/api/v1/mindmaps/from-generated-data
Body:
{
  "generatedDataId": 1,
  "templateId": 1,
  "name": "Mindmap Hàm số bậc hai",
  "sharedStatus": "PRIVATE",
  "collectionId": null
}

Response:
{
  "code": 200,
  "message": "Mindmap created from generated data successfully",
  "data": {
    "id": 1,
    "name": "Mindmap Hàm số bậc hai",
    "sharedStatus": "PRIVATE",
    "collectionId": 1,
    "templateId": 1
  }
}
```

---

### **Test 6: Lấy Danh Sách Conversations**
```
GET http://localhost:8080/api/v1/chat/conversations?userId=1

Response:
{
  "code": 200,
  "message": "Conversations retrieved successfully",
  "data": [
    {
      "conversationId": 1,
      "title": "Test Conversation",
      "isActive": true,
      "messageCount": 3,
      "createdAt": "2025-11-14T05:30:00Z",
      "updatedAt": "2025-11-14T05:35:00Z"
    }
  ]
}
```

---

## 🔐 YÊU CẦU API KEY TRONG DB

Để hệ thống hoạt động, user phải có API key active trong bảng `api_keys`:

```sql
-- Kiểm tra user có API key không
SELECT * FROM api_keys 
WHERE user_id = 1 
  AND is_active = true 
  AND remaining_calls > 0
  AND (expired_at IS NULL OR expired_at > NOW());
```

**Nếu không có API key hợp lệ**:
```json
{
  "code": 500,
  "message": "No active API key found for user. Please purchase a package.",
  "data": null
}
```

---

## ⚠️ LƯU Ý QUAN TRỌNG

### 1. **Security đang MỞ HOÀN TOÀN**
- ✅ Swagger hoạt động bình thường
- ✅ Postman không cần token
- ❌ KHÔNG được deploy production như vậy
- 🔒 Sau khi test xong, cần bật lại JWT

### 2. **API Key phải có trong DB**
- Hệ thống KHÔNG dùng `.env` nữa
- API key lấy từ bảng `api_keys`
- Phải có order COMPLETED để key active

### 3. **Validation Toán 10-11-12**
- Hệ thống TỰ ĐỘNG từ chối topic ngoài phạm vi
- KHÔNG gọi AI nếu topic không hợp lệ
- KHÔNG trừ remaining_calls nếu từ chối

### 4. **Knowledge JSON**
- Lưu trong `generated_datas.knowledge_json` (JSONB)
- Format: MAP từ nodeId → array of knowledge items
- Hỗ trợ LaTeX trong `contentMarkdown`

### 5. **Response Format**
- TẤT CẢ endpoint đều trả `ResponseBase`
- Format: `{code, message, data}`
- Exception được handle bởi `@ControllerAdvice`

---

## 🎓 KIẾN THỨC TOÁN ĐƯỢC HỖ TRỢ

### **Lớp 10**:
- Hàm số, phương trình, bất phương trình
- Tập hợp, mệnh đề
- Hệ thức lượng trong tam giác
- Đường tròn, elip, parabol, hyperbol
- Vectơ, tọa độ

### **Lớp 11**:
- Lượng giác
- Dãy số, cấp số cộng, cấp số nhân
- Giới hạn, đạo hàm
- Đường thẳng và mặt phẳng trong không gian
- Khối đa diện, mặt cầu, mặt nón, mặt trụ

### **Lớp 12**:
- Khảo sát hàm số
- Logarit và hàm mũ
- Tích phân, nguyên hàm
- Số phức
- Xác suất, thống kê, tổ hợp

---

## 🚀 NEXT STEPS (TÙY CHỌN)

1. **Thêm rate limiting** - Giới hạn số request/phút
2. **Cache OpenAI response** - Tránh gọi lại topic giống nhau
3. **Webhook notification** - Thông báo khi mindmap tạo xong
4. **Export mindmap** - PDF, PNG, JSON
5. **Share mindmap** - Public URL
6. **Version control** - Lưu nhiều version của mindmap

---

## 📞 SUPPORT

Nếu có lỗi, kiểm tra:
1. ✅ User có API key active không?
2. ✅ API key còn remaining_calls không?
3. ✅ Topic có chứa từ khóa Toán không?
4. ✅ Grade có phải 10/11/12 không?
5. ✅ Database connection OK không?
6. ✅ OpenAI API key trong DB còn hợp lệ không?

**Log level**: INFO  
**Emoji**: Tất cả log đều có emoji để dễ trace 🔍

---

**✅ HỆ THỐNG ĐÃ SẴN SÀNG TEST!**

