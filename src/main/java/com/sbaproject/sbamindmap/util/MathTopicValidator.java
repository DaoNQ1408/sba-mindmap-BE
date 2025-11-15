package com.sbaproject.sbamindmap.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Validator để kiểm tra xem chủ đề có phải Toán THPT (10-11-12) hay không
 * Dựa trên chương trình Toán THPT Việt Nam đầy đủ
 */
@Component
@Slf4j
public class MathTopicValidator {

    // Danh sách từ khóa Toán THPT
    private static final List<String> VALID_GRADES = Arrays.asList("10", "11", "12");

    private static final List<String> MATH_KEYWORDS = Arrays.asList(
            // ============ TOÁN 10 - ĐẠI SỐ ============
            // Mệnh đề – Tập hợp – Số thực
            "mệnh đề", "mệnh đề phủ định", "mệnh đề kéo theo", "phủ định",
            "tập hợp", "phần tử", "bao hàm", "hợp", "giao", "hiệu", "phần bù",
            "số thực", "trục số", "khoảng", "đoạn", "tập con", "ánh xạ",

            // Hàm số & đồ thị
            "hàm số", "đồ thị", "hàm số bậc nhất", "hàm số bậc hai", "hàm bậc nhất", "hàm bậc hai",
            "bảng biến thiên", "cực trị", "cực đại", "cực tiểu",
            "đường thẳng", "parabol", "parapol", "tính đơn điệu", "đồng biến", "nghịch biến",

            // Phương trình – Bất phương trình
            "phương trình", "phương trình bậc nhất", "phương trình bậc hai",
            "bất phương trình", "bất phương trình bậc nhất", "bất phương trình bậc hai",
            "công thức nghiệm", "biện luận phương trình", "biện luận",
            "bất đẳng thức", "hệ phương trình", "cramer", "định thức",

            // Thống kê – Xác suất
            "thống kê", "bảng tần số", "tần suất", "số trung bình", "trung bình",
            "mốt", "trung vị", "xác suất", "xác suất cổ điển",
            "biến cố", "không gian mẫu",

            // ============ TOÁN 10 - HÌNH HỌC ============
            // Vecto
            "vecto", "vector", "độ dài vecto", "điểm đặt",
            "phép cộng vecto", "phép trừ vecto", "nhân vô hướng",
            "tích vô hướng", "góc giữa hai vecto", "biểu diễn tọa độ",

            // Tọa độ trong mặt phẳng Oxy
            "tọa độ", "mặt phẳng oxy", "oxy", "tọa độ điểm", "tọa độ vecto",
            "phương trình đường thẳng", "phương trình tổng quát", "phương trình tham số",
            "góc", "khoảng cách", "khoảng cách từ điểm đến đường thẳng",
            "phương trình đường tròn", "đường tròn", "tâm", "bán kính",
            "vị trí tương đối", "đường thẳng và đường tròn",

            // ============ TOÁN 11 - ĐẠI SỐ & GIẢI TÍCH ============
            // Hàm số lượng giác
            "lượng giác", "hàm lượng giác", "sin", "cos", "tan", "cot",
            "giá trị lượng giác", "công thức lượng giác",
            "công thức cộng", "công thức nhân đôi", "công thức hạ bậc",
            "phương trình lượng giác", "phương trình lượng giác cơ bản",

            // Cấp số
            "cấp số", "cấp số cộng", "cấp số nhân",
            "số hạng tổng quát", "tổng n số hạng", "công sai", "công bội",

            // Tổ hợp – Xác suất nâng cao
            "hoán vị", "chỉnh hợp", "tổ hợp",
            "nhị thức newton", "quy tắc nhân", "quy tắc cộng",
            "xác suất có điều kiện", "biến cố độc lập", "công thức nhân",

            // Giới hạn & hàm số
            "dãy số", "giới hạn", "giới hạn dãy số", "giới hạn hàm số",
            "giới hạn vô cực", "hàm số liên tục", "liên tục",

            // ============ TOÁN 11 - HÌNH HỌC KHÔNG GIAN ============
            "hình học không gian", "điểm", "đường", "mặt phẳng",
            "hai đường thẳng chéo nhau", "đường thẳng chéo nhau",
            "góc giữa hai đường thẳng", "góc giữa đường và mặt", "góc giữa hai mặt",
            "quan hệ song song", "quan hệ vuông góc", "song song", "vuông góc",

            // Phép biến hình
            "phép biến hình", "phép tịnh tiến", "phép đối xứng", "đối xứng trục", "đối xứng tâm",
            "phép quay", "phép vị tự", "phép đồng dạng", "đồng dạng",

            // ============ TOÁN 12 - GIẢI TÍCH ============
            // Hàm số – khảo sát – đồ thị
            "đạo hàm", "đạo hàm cấp 1", "đạo hàm cấp 2", "đạo hàm bậc hai",
            "ý nghĩa hình học", "khảo sát hàm số", "khảo sát",
            "điểm uốn", "tiệm cận", "tiệm cận đứng", "tiệm cận ngang", "tiệm cận xiên",
            "hàm bậc ba", "hàm trùng phương", "hàm phân thức",

            // Mũ – Logarit
            "mũ", "lũy thừa", "hàm mũ", "hàm số mũ",
            "logarit", "lôgarit", "log", "ln",
            "phương trình mũ", "phương trình logarit",
            "bất phương trình mũ", "bất phương trình logarit",
            "tăng trưởng", "lãi suất", "phóng xạ",

            // Nguyên hàm – Tích phân
            "nguyên hàm", "tích phân", "tích phân xác định",
            "bảng nguyên hàm", "diện tích", "thể tích",
            "phương pháp đổi biến", "phương pháp từng phần", "đổi biến", "từng phần",

            // Số phức
            "số phức", "biểu diễn hình học", "dạng đại số", "môđun", "arg",
            "phép cộng số phức", "phép nhân số phức", "phép chia số phức",
            "phương trình bậc hai trong c", "căn bậc hai số phức",

            // ============ TOÁN 12 - HÌNH HỌC KHÔNG GIAN ============
            // Khối đa diện
            "khối đa diện", "hình chóp", "khối lăng trụ", "lăng trụ",
            "diện tích xung quanh", "diện tích toàn phần",
            "thể tích khối đa diện", "mặt phẳng cắt khối đa diện",

            // Mặt tròn xoay
            "mặt tròn xoay", "hình nón", "khối nón", "mặt nón",
            "hình trụ", "khối trụ", "mặt trụ",
            "hình cầu", "khối cầu", "mặt cầu",
            "diện tích mặt cầu", "diện tích mặt nón", "diện tích mặt trụ",
            "thể tích khối nón", "thể tích khối trụ", "thể tích khối cầu",

            // Tọa độ trong không gian Oxyz
            "không gian oxyz", "oxyz", "tọa độ trong không gian",
            "vecto trong không gian", "phương trình mặt phẳng",
            "phương trình đường thẳng trong không gian",
            "khoảng cách điểm đến mặt phẳng", "khoảng cách điểm đến đường thẳng",
            "góc giữa các đối tượng",

            // ============ TỪ KHÓA CHUNG ============
            "toán", "toán học", "đại số", "hình học", "giải tích",
            "tam giác", "elip", "hyperbol", "hệ thức lượng"
    );

    /**
     * Kiểm tra xem grade có hợp lệ không
     */
    public boolean isValidGrade(String grade) {
        if (grade == null || grade.trim().isEmpty()) {
            return false;
        }
        return VALID_GRADES.contains(grade.trim());
    }

    /**
     * Kiểm tra xem topic có phải là chủ đề Toán THPT không
     */
    public boolean isValidMathTopic(String topic) {
        if (topic == null || topic.trim().isEmpty()) {
            return false;
        }

        String normalizedTopic = topic.toLowerCase().trim();

        // Kiểm tra xem có chứa từ khóa Toán không
        boolean hasKeyword = MATH_KEYWORDS.stream()
                .anyMatch(keyword -> normalizedTopic.contains(keyword.toLowerCase()));

        log.debug("Topic validation - Topic: '{}', Has keyword: {}", topic, hasKeyword);

        return hasKeyword;
    }

    /**
     * Kiểm tra toàn bộ request
     */
    public ValidationResult validate(String topic, String grade) {
        if (!isValidGrade(grade)) {
            return ValidationResult.invalid("Khối lớp phải là 10, 11 hoặc 12");
        }

        if (!isValidMathTopic(topic)) {
            return ValidationResult.invalid(
                    "Hệ thống chỉ hỗ trợ kiến thức Toán THPT lớp 10-11-12. Vui lòng hỏi lại trong phạm vi này."
            );
        }

        return ValidationResult.valid();
    }

    /**
     * Kết quả validation
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        private ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult invalid(String errorMessage) {
            return new ValidationResult(false, errorMessage);
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
