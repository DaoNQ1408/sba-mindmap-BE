# 📚 API Documentation for Frontend

## 🔐 Authentication Flow

### Base URL
```
http://localhost:8080
```

---

## 1️⃣ Authentication APIs (PUBLIC)

### 1.1 Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "@1"
}
```

**Response:**
```json
{
  "code": 200,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYzOTU4...",
    "type": "Bearer",
    "username": "admin",
    "role": "ADMIN",
    "userId": 1
  }
}
```

### 1.2 Register (if available)
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "email": "newuser@gmail.com",
  "password": "password123",
  "fullName": "New User"
}
```

---

## 🔑 Using JWT Token

**Tất cả các API bên dưới đều cần gửi JWT token trong header:**

```http
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYzOTU4...
```

---

## 2️⃣ API Keys (USER + ADMIN)

### 2.1 Get Available API Keys
```http
GET /api/keys/available?uid=1
Authorization: Bearer {token}
```

**Response:**
```json
[
  {
    "apiKeyId": 1,
    "keyValue": "sk-proj-...",
    "remainingCalls": 99998,
    "isActive": true,
    "activatedAt": "2025-11-14T08:06:07.928540Z",
    "expiredAt": "2026-11-14T08:06:07.928540Z"
  }
]
```

### 2.2 Check API Key Validity
```http
GET /api/keys/{keyId}/check?uid=1
Authorization: Bearer {token}
```

**Response:**
```json
true
```

---

## 3️⃣ Templates (GET: USER + ADMIN, POST/PUT/DELETE: ADMIN only)

### 3.1 Get All Templates
```http
GET /api/templates
Authorization: Bearer {token}
```

**Response:**
```json
{
  "code": 200,
  "message": "Templates retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Modern Math - Blue",
      "styleConfig": {
        "pane": { "backgroundColor": "#f0f4f8" },
        "defaultNode": {
          "backgroundColor": "#3b82f6",
          "color": "#ffffff",
          "border": "2px solid #2563eb",
          "borderRadius": 8
        },
        "defaultEdge": {
          "stroke": "#3b82f6",
          "strokeWidth": 2,
          "animated": true
        }
      },
      "createdAt": "2025-11-14T01:10:27"
    }
  ]
}
```

### 3.2 Get Template by ID
```http
GET /api/templates/{id}
Authorization: Bearer {token}
```

### 3.3 Create Template (ADMIN only)
```http
POST /api/templates
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Custom Template",
  "styleConfig": { /* ... */ }
}
```

### 3.4 Update Template (ADMIN only)
```http
PUT /api/templates/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Updated Template",
  "styleConfig": { /* ... */ }
}
```

### 3.5 Delete Template (ADMIN only)
```http
DELETE /api/templates/{id}
Authorization: Bearer {token}
```

---

## 4️⃣ Chat & Mindmap Generation (USER + ADMIN)

### 4.1 Generate Mindmap from Math Topic
```http
POST /api/chat/generate-mindmap
Authorization: Bearer {token}
Content-Type: application/json

{
  "topic": "Hàm số bậc hai",
  "grade": "10",
  "userId": "1"
}
```

**Response:**
```json
{
  "code": 200,
  "message": "Mindmap generated successfully",
  "data": {
    "userMessage": "Tạo mindmap về Hàm số bậc hai",
    "assistantMessage": "{\"nodes\":[...],\"edges\":[...],\"knowledge\":{...}}",
    "conversationId": 123,
    "generatedDataId": 45
  }
}
```

### 4.2 Create Conversation
```http
POST /api/chat/conversation?userId=1&title=Học toán 12
Authorization: Bearer {token}
```

**Response:**
```json
{
  "code": 200,
  "message": "Conversation created successfully",
  "data": {
    "conversationId": 123,
    "userId": "1",
    "title": "Học toán 12",
    "createdAt": "2025-11-14T19:30:00"
  }
}
```

### 4.3 Send Chat Message
```http
POST /api/chat/message
Authorization: Bearer {token}
Content-Type: application/json

{
  "userId": "1",
  "conversationId": 123,
  "message": "Giải thích về đạo hàm"
}
```

**Response:**
```json
{
  "code": 200,
  "message": "Message sent successfully",
  "data": {
    "userMessage": "Giải thích về đạo hàm",
    "assistantMessage": "Đạo hàm là...",
    "conversationId": 123
  }
}
```

### 4.4 Get Conversation History
```http
GET /api/chat/conversation/{conversationId}/history
Authorization: Bearer {token}
```

**Response:**
```json
{
  "code": 200,
  "message": "Conversation history retrieved successfully",
  "data": [
    {
      "role": "user",
      "content": "Giải thích về đạo hàm",
      "timestamp": "2025-11-14T19:45:10"
    },
    {
      "role": "assistant",
      "content": "Đạo hàm là...",
      "timestamp": "2025-11-14T19:45:12"
    }
  ]
}
```

### 4.5 Get User Conversations
```http
GET /api/chat/conversations?userId=1
Authorization: Bearer {token}
```

---

## 5️⃣ Conversations Management (USER + ADMIN)

### 5.1 Start Conversation
```http
POST /api/conversations/start?uid=1&apiKeyId=1&title=Học toán
Authorization: Bearer {token}
```

### 5.2 Send Message in Conversation
```http
POST /api/conversations/{conversationId}/message
Authorization: Bearer {token}
Content-Type: application/json

{
  "userId": 1,
  "prompt": "Giải thích về đạo hàm"
}
```

### 5.3 Get Conversation with Messages
```http
GET /api/conversations/{conversationId}/full
Authorization: Bearer {token}
```

### 5.4 Get All User Conversations
```http
GET /api/conversations/user?uid=1
Authorization: Bearer {token}
```

### 5.5 Delete Conversation
```http
DELETE /api/conversations/{conversationId}
Authorization: Bearer {token}
```

---

## 6️⃣ Generated Data (GET by ID: USER + ADMIN, Others: ADMIN only)

### 6.1 Get Generated Data by ID
```http
GET /api/generated-data/{id}
Authorization: Bearer {token}
```

**Response:**
```json
{
  "code": 200,
  "message": "Generated data retrieved successfully",
  "data": {
    "generatedDataId": 1,
    "nodes": "[{\"id\":\"1\",\"data\":{\"label\":\"Hàm số bậc hai\"}}]",
    "edges": "[{\"id\":\"e1-2\",\"source\":\"1\",\"target\":\"2\"}]",
    "knowledgeJson": "{\"1\":[{\"type\":\"definition\",\"title\":\"...\"}]}",
    "isChecked": false,
    "createdAt": "2025-11-14T19:30:00"
  }
}
```

### 6.2 Get All Generated Data (ADMIN only)
```http
GET /api/generated-data
Authorization: Bearer {token}
```

### 6.3 Mark as Checked (ADMIN only)
```http
PATCH /api/generated-data/{id}/check
Authorization: Bearer {token}
```

### 6.4 Delete Generated Data (ADMIN only)
```http
DELETE /api/generated-data/{id}
Authorization: Bearer {token}
```

---

## 7️⃣ Mindmaps (GET/POST/PUT: USER + ADMIN, DELETE: ADMIN only)

### 7.1 Get All Mindmaps
```http
GET /api/mindmaps
Authorization: Bearer {token}
```

**Response:**
```json
{
  "code": 200,
  "message": "Mindmaps retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Mindmap Hàm số bậc hai",
      "sharedStatus": "PRIVATE",
      "createdAt": "2025-11-14T19:30:00"
    }
  ]
}
```

### 7.2 Get Mindmap by ID
```http
GET /api/mindmaps/{id}
Authorization: Bearer {token}
```

### 7.3 Get Mindmap Detail for ReactFlow Rendering
```http
GET /api/mindmaps/{id}/detail
Authorization: Bearer {token}
```

**Response:**
```json
{
  "code": 200,
  "message": "Mindmap detail retrieved successfully",
  "data": {
    "mindmapId": 1,
    "name": "Mindmap Hàm số bậc hai",
    "nodes": [
      {
        "id": "1",
        "data": { "label": "Hàm số bậc hai" },
        "position": { "x": 0, "y": 0 }
      }
    ],
    "edges": [
      {
        "id": "e1-2",
        "source": "1",
        "target": "2"
      }
    ],
    "knowledge": {
      "1": [
        {
          "type": "definition",
          "title": "Định nghĩa hàm số bậc hai",
          "contentMarkdown": "Hàm số bậc hai có dạng $$y = ax^2 + bx + c$$"
        }
      ]
    },
    "template": {
      "id": 1,
      "name": "Modern Math - Blue",
      "styleConfig": { /* ... */ }
    },
    "sharedStatus": "PRIVATE",
    "createdAt": "2025-11-14T19:30:00"
  }
}
```

### 7.4 Create Mindmap
```http
POST /api/mindmaps
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "My Mindmap",
  "templateId": 1,
  "sharedStatus": "PRIVATE"
}
```

### 7.5 Create Mindmap from Generated Data
```http
POST /api/mindmaps/from-generated-data
Authorization: Bearer {token}
Content-Type: application/json

{
  "generatedDataId": 45,
  "templateId": 1,
  "name": "AI Generated Mindmap",
  "sharedStatus": "PRIVATE",
  "collectionId": null
}
```

### 7.6 Update Mindmap
```http
PUT /api/mindmaps/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Updated Mindmap Name",
  "templateId": 2,
  "sharedStatus": "PUBLIC"
}
```

### 7.7 Delete Mindmap (ADMIN only)
```http
DELETE /api/mindmaps/{id}
Authorization: Bearer {token}
```

---

## 8️⃣ Test Accounts

### ADMIN Account
```
Username: admin
Password: @1
Role: ADMIN
User ID: 1
```

### USER Account (Unlimited)
```
Username: testuser1
Password: password123
Role: USER
User ID: 5
API Calls: 99999
```

### USER Account (Limited)
```
Username: testuser2
Password: password123
Role: USER
User ID: 6
API Calls: 2
```

---

## 🚨 Error Handling

### Common Error Responses

**401 Unauthorized (No token or invalid token):**
```json
{
  "timestamp": "2025-11-14T19:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/mindmaps"
}
```

**403 Forbidden (Insufficient permissions):**
```json
{
  "timestamp": "2025-11-14T19:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/templates"
}
```

**404 Not Found:**
```json
{
  "code": 404,
  "message": "Mindmap not found with ID: 999",
  "data": null
}
```

**400 Bad Request:**
```json
{
  "code": 400,
  "message": "Invalid request data",
  "data": null
}
```

---

## 📝 Frontend Implementation Example (JavaScript/TypeScript)

### Setup Axios Interceptor
```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
});

// Add JWT token to every request
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Handle 401 errors (redirect to login)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('jwt_token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

### Login Example
```javascript
const login = async (username, password) => {
  try {
    const response = await api.post('/api/auth/login', {
      username,
      password,
    });
    
    const { token, role, userId } = response.data.data;
    
    // Store token and user info
    localStorage.setItem('jwt_token', token);
    localStorage.setItem('user_role', role);
    localStorage.setItem('user_id', userId);
    
    return response.data;
  } catch (error) {
    console.error('Login failed:', error);
    throw error;
  }
};
```

### Get Templates Example
```javascript
const getTemplates = async () => {
  try {
    const response = await api.get('/api/templates');
    return response.data.data; // Array of templates
  } catch (error) {
    console.error('Failed to fetch templates:', error);
    throw error;
  }
};
```

### Generate Mindmap Example
```javascript
const generateMindmap = async (topic, grade) => {
  try {
    const response = await api.post('/api/chat/generate-mindmap', {
      topic,
      grade,
      userId: localStorage.getItem('user_id'),
    });
    
    return response.data.data;
  } catch (error) {
    console.error('Failed to generate mindmap:', error);
    throw error;
  }
};
```

### Create Mindmap from Generated Data
```javascript
const createMindmapFromData = async (generatedDataId, templateId, name) => {
  try {
    const response = await api.post('/api/mindmaps/from-generated-data', {
      generatedDataId,
      templateId,
      name,
      sharedStatus: 'PRIVATE',
      collectionId: null,
    });
    
    return response.data.data;
  } catch (error) {
    console.error('Failed to create mindmap:', error);
    throw error;
  }
};
```

### Get Mindmap Detail for ReactFlow
```javascript
const getMindmapDetail = async (mindmapId) => {
  try {
    const response = await api.get(`/api/mindmaps/${mindmapId}/detail`);
    const data = response.data.data;
    
    // Data ready for ReactFlow
    return {
      nodes: data.nodes,
      edges: data.edges,
      knowledge: data.knowledge,
      template: data.template,
    };
  } catch (error) {
    console.error('Failed to fetch mindmap detail:', error);
    throw error;
  }
};
```

---

## 🎯 Role-Based UI Rendering

```javascript
const isAdmin = () => {
  return localStorage.getItem('user_role') === 'ADMIN';
};

// Conditional rendering example (React)
{isAdmin() && (
  <button onClick={deleteTemplate}>Delete Template</button>
)}

{isAdmin() && (
  <button onClick={deleteMindmap}>Delete Mindmap</button>
)}
```

---

## 📊 API Summary Table

| Endpoint | Method | USER | ADMIN | Description |
|----------|--------|------|-------|-------------|
| `/api/auth/**` | ALL | ✅ | ✅ | Public (no auth needed) |
| `/api/keys/available` | GET | ✅ | ✅ | Get available API keys |
| `/api/generated-data/{id}` | GET | ✅ | ✅ | Get generated data by ID |
| `/api/generated-data/**` | ALL | ❌ | ✅ | Manage generated data |
| `/api/templates` | GET | ✅ | ✅ | View templates |
| `/api/templates/**` | POST/PUT/DELETE | ❌ | ✅ | Manage templates |
| `/api/mindmaps` | GET | ✅ | ✅ | View all mindmaps |
| `/api/mindmaps/{id}/detail` | GET | ✅ | ✅ | Get mindmap for ReactFlow |
| `/api/mindmaps/from-generated-data` | POST | ✅ | ✅ | Create mindmap from AI data |
| `/api/mindmaps/{id}` | PUT | ✅ | ✅ | Update mindmap |
| `/api/mindmaps/{id}` | DELETE | ❌ | ✅ | Delete mindmap |
| `/api/chat/**` | ALL | ✅ | ✅ | Chat & generate mindmap |
| `/api/conversations/**` | ALL | ✅ | ✅ | Manage conversations |
| `/api/chatgpt/**` | ALL | ❌ | ✅ | Test ChatGPT API |
| `/api/gemini/**` | ALL | ❌ | ✅ | Test Gemini API |

---

## 🚀 Quick Start Checklist for Frontend

- [ ] 1. Setup Axios with base URL `http://localhost:8080`
- [ ] 2. Implement login page (call `/api/auth/login`)
- [ ] 3. Store JWT token in localStorage
- [ ] 4. Add Authorization header to all requests
- [ ] 5. Handle 401 (redirect to login) and 403 (show access denied)
- [ ] 6. Implement role-based UI rendering (show/hide admin features)
- [ ] 7. Test with ADMIN account first, then USER account
- [ ] 8. Implement mindmap generation flow:
   - Generate mindmap → Get generatedDataId
   - Create mindmap from generatedDataId
   - Fetch mindmap detail → Render in ReactFlow

---

**📧 Questions? Contact Backend Team!**

