package com.sbaproject.sbamindmap.config;

import com.sbaproject.sbamindmap.entity.*;
import com.sbaproject.sbamindmap.enums.OrderStatus;
import com.sbaproject.sbamindmap.enums.UserRole;
import com.sbaproject.sbamindmap.enums.UserStatus;
import com.sbaproject.sbamindmap.pojo.template.EdgeStyle;
import com.sbaproject.sbamindmap.pojo.template.NodeStyle;
import com.sbaproject.sbamindmap.pojo.template.PaneStyle;
import com.sbaproject.sbamindmap.pojo.template.StyleConfig;
import com.sbaproject.sbamindmap.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PackagesRepository packagesRepository;
    private final OderRepository ordersRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final TemplateRepository templateRepository;

    @Value("${OPENAI_API_KEY:}")
    private String openaiApiKey;

    @Override
    public void run(String... args) {

        // ========== 1. ADMIN USER ==========
        User admin = userRepository.getByMail("admin@gmail.com");
        if (admin == null) {
            admin = new User();
            admin.setMail("admin@gmail.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("@1"));
            admin.setFullName("Nguyen Van A");
            admin.setRole(UserRole.ADMIN);
            admin.setUserStatus(UserStatus.ACTIVE);
            userRepository.save(admin);
            System.out.println(" Created ADMIN user");
        } else {
            System.out.println("ADMIN user already exists - skipping");
        }
        final User finalAdmin = admin; // Make it final for lambda

        // ========== 2. VNPAY PAYMENT METHOD ==========
        Optional<PaymentMethod> existingVnpay = paymentMethodRepository.findByName("VNPAY");
        if (existingVnpay.isEmpty()) {
            PaymentMethod pm = new PaymentMethod();
            pm.setName("VNPAY");
            pm.setIsActive(true);
            paymentMethodRepository.save(pm);
            System.out.println(" Created VNPAY payment method");
        } else {
            System.out.println("  VNPAY payment method already exists - skipping");
        }

        // ========== 2.5. CREATE SAMPLE TEMPLATE ==========
        createSampleTemplate();

        // ========== 2.7. CREATE ADMIN PACKAGE & ORDER ==========
        Packages adminPackage = packagesRepository.findAll().stream()
                .filter(p -> "Unlimited Package".equals(p.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Packages pkg = new Packages();
                    pkg.setName("Unlimited Package");
                    pkg.setPrice(999.99);
                    pkg.setApiCallLimit("99999");
                    pkg.setCreatedAt(LocalDateTime.now());
                    pkg.setUpdatedAt(LocalDateTime.now());
                    Packages saved = packagesRepository.save(pkg);
                    System.out.println(" Created Unlimited Package");
                    return saved;
                });

        // Tạo Order cho ADMIN
        boolean hasAdminOrder = ordersRepository.findAll().stream()
                .anyMatch(o -> o.getUser() != null && o.getUser().getId().equals(finalAdmin.getId())
                        && o.getPackages() != null && o.getPackages().getPackageId() == adminPackage.getPackageId());

        if (!hasAdminOrder) {
            Orders order = new Orders();
            order.setUser(finalAdmin);
            order.setPackages(adminPackage);
            order.setStatus(OrderStatus.COMPLETED);
            order.setOrderDate(LocalDateTime.now());
            order.setCompletedAt(LocalDateTime.now());
            ordersRepository.save(order);
            System.out.println("Created Order for ADMIN");
        } else {
            System.out.println(" Order for ADMIN already exists - skipping");
        }

        // Tạo API Key cho ADMIN
        if (openaiApiKey != null && !openaiApiKey.isEmpty()) {
            createOrUpdateApiKey(finalAdmin, adminPackage, openaiApiKey, 99999, 365, "ADMIN");
        } else {
            System.out.println(" OpenAI API Key not found in environment - skipping API key creation for ADMIN");
        }

        // ========== 3. TEST USER 1 - UNLIMITED CALLS (99999) ==========
        User testUser1 = userRepository.getByMail("testuser1@gmail.com");
        if (testUser1 == null) {
            testUser1 = new User();
            testUser1.setMail("testuser1@gmail.com");
            testUser1.setUsername("testuser1");
            testUser1.setPassword(passwordEncoder.encode("password123"));
            testUser1.setFullName("Test User Unlimited");
            testUser1.setRole(UserRole.USER);
            testUser1.setUserStatus(UserStatus.ACTIVE);
            testUser1 = userRepository.save(testUser1);
            System.out.println(" Created Test User 1 (Unlimited)");
        } else {
            System.out.println("ℹ Test User 1 already exists - skipping user creation");
        }
        final User finalTestUser1 = testUser1; // Make it final for lambda

        // Tạo Package cho User 1 (Unlimited)
        Packages unlimitedPackage = packagesRepository.findAll().stream()
                .filter(p -> "Unlimited Package".equals(p.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Packages pkg = new Packages();
                    pkg.setName("Unlimited Package");
                    pkg.setPrice(999.99);
                    pkg.setApiCallLimit("99999");
                    pkg.setCreatedAt(LocalDateTime.now());
                    pkg.setUpdatedAt(LocalDateTime.now());
                    Packages saved = packagesRepository.save(pkg);
                    System.out.println(" Created Unlimited Package");
                    return saved;
                });

        // Kiểm tra Order đã tồn tại chưa
        boolean hasOrder1 = ordersRepository.findAll().stream()
                .anyMatch(o -> o.getUser() != null && o.getUser().getId().equals(finalTestUser1.getId())
                        && o.getPackages() != null && o.getPackages().getPackageId() == unlimitedPackage.getPackageId());

        if (!hasOrder1) {
            Orders order = new Orders();
            order.setUser(finalTestUser1);
            order.setPackages(unlimitedPackage);
            order.setStatus(OrderStatus.COMPLETED);
            order.setOrderDate(LocalDateTime.now());
            order.setCompletedAt(LocalDateTime.now());
            ordersRepository.save(order);
            System.out.println(" Created Order for Test User 1");
        } else {
            System.out.println(" Order for Test User 1 already exists - skipping");
        }

        // Tạo hoặc cập nhật API Key cho User 1
        if (openaiApiKey != null && !openaiApiKey.isEmpty()) {
            createOrUpdateApiKey(finalTestUser1, unlimitedPackage, openaiApiKey, 99999, 365, "Test User 1");
        } else {
            System.out.println(" OpenAI API Key not found in environment - skipping API key creation for Test User 1");
        }

        // ========== 4. TEST USER 2 - LIMITED CALLS (2) ==========
        User testUser2 = userRepository.getByMail("testuser2@gmail.com");
        if (testUser2 == null) {
            testUser2 = new User();
            testUser2.setMail("testuser2@gmail.com");
            testUser2.setUsername("testuser2");
            testUser2.setPassword(passwordEncoder.encode("password123"));
            testUser2.setFullName("Test User Limited");
            testUser2.setRole(UserRole.USER);
            testUser2.setUserStatus(UserStatus.ACTIVE);
            testUser2 = userRepository.save(testUser2);
            System.out.println("Created Test User 2 (Limited)");
        } else {
            System.out.println("ℹ Test User 2 already exists - skipping user creation");
        }
        final User finalTestUser2 = testUser2; // Make it final for lambda

        // Tạo Package cho User 2 (Limited)
        Packages limitedPackage = packagesRepository.findAll().stream()
                .filter(p -> "Trial Package".equals(p.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Packages pkg = new Packages();
                    pkg.setName("Trial Package");
                    pkg.setPrice(0.0);
                    pkg.setApiCallLimit("2");
                    pkg.setCreatedAt(LocalDateTime.now());
                    pkg.setUpdatedAt(LocalDateTime.now());
                    Packages saved = packagesRepository.save(pkg);
                    System.out.println("Created Trial Package");
                    return saved;
                });

        // Kiểm tra Order đã tồn tại chưa
        boolean hasOrder2 = ordersRepository.findAll().stream()
                .anyMatch(o -> o.getUser() != null && o.getUser().getId().equals(finalTestUser2.getId())
                        && o.getPackages() != null && o.getPackages().getPackageId() == limitedPackage.getPackageId());

        if (!hasOrder2) {
            Orders order = new Orders();
            order.setUser(finalTestUser2);
            order.setPackages(limitedPackage);
            order.setStatus(OrderStatus.COMPLETED);
            order.setOrderDate(LocalDateTime.now());
            order.setCompletedAt(LocalDateTime.now());
            ordersRepository.save(order);
            System.out.println("Created Order for Test User 2");
        } else {
            System.out.println(" Order for Test User 2 already exists - skipping");
        }

        // Tạo hoặc cập nhật API Key cho User 2
        if (openaiApiKey != null && !openaiApiKey.isEmpty()) {
            createOrUpdateApiKey(finalTestUser2, limitedPackage, openaiApiKey, 2, 30, "Test User 2");
        } else {
            System.out.println("⚠  OpenAI API Key not found in environment - skipping API key creation for Test User 2");
        }

        System.out.println("\n Data initialization completed!");
        System.out.println(" Summary:");
        System.out.println("   - Admin: admin@gmail.com / @1");
        System.out.println("   - Test User 1 (Unlimited): testuser1@gmail.com / password123 - Calls: 99999");
        System.out.println("   - Test User 2 (Limited): testuser2@gmail.com / password123 - Calls: 2");
        System.out.println("   - OpenAI API Key: " + (openaiApiKey != null && !openaiApiKey.isEmpty() ? "Configured " : "NOT FOUND "));
        System.out.println("   - Sample Templates: Created ");
    }

    /**
     * Tạo template mẫu cho mindmap toán học THPT
     */
    private void createSampleTemplate() {
        // Template 1: Modern Math Template (Blue Theme)
        if (templateRepository.findByName("Modern Math - Blue").isEmpty()) {
            StyleConfig blueStyle = StyleConfig.builder()
                    .pane(PaneStyle.builder()
                            .backgroundColor("#f0f4f8")
                            .build())
                    .defaultNode(NodeStyle.builder()
                            .backgroundColor("#3b82f6")
                            .color("#ffffff")
                            .border("2px solid #2563eb")
                            .borderRadius(8)
                            .fontSize(14)
                            .padding(12)
                            .build())
                    .defaultEdge(EdgeStyle.builder()
                            .stroke("#3b82f6")
                            .strokeWidth(2)
                            .animated(true)
                            .type("smoothstep")
                            .build())
                    .build();

            Template blueTemplate = Template.builder()
                    .name("Modern Math - Blue")
                    .styleConfig(blueStyle)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            templateRepository.save(blueTemplate);
            System.out.println(" Created Template: Modern Math - Blue");
        } else {
            System.out.println("  Template 'Modern Math - Blue' already exists - skipping");
        }

        // Template 2: Classic Math Template (Green Theme)
        if (templateRepository.findByName("Classic Math - Green").isEmpty()) {
            StyleConfig greenStyle = StyleConfig.builder()
                    .pane(PaneStyle.builder()
                            .backgroundColor("#f0fdf4")
                            .build())
                    .defaultNode(NodeStyle.builder()
                            .backgroundColor("#10b981")
                            .color("#ffffff")
                            .border("2px solid #059669")
                            .borderRadius(12)
                            .fontSize(14)
                            .padding(16)
                            .build())
                    .defaultEdge(EdgeStyle.builder()
                            .stroke("#10b981")
                            .strokeWidth(2)
                            .animated(false)
                            .type("default")
                            .build())
                    .build();

            Template greenTemplate = Template.builder()
                    .name("Classic Math - Green")
                    .styleConfig(greenStyle)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            templateRepository.save(greenTemplate);
            System.out.println(" Created Template: Classic Math - Green");
        } else {
            System.out.println("️  Template 'Classic Math - Green' already exists - skipping");
        }

        // Template 3: Dark Math Template (Purple Theme)
        if (templateRepository.findByName("Dark Math - Purple").isEmpty()) {
            StyleConfig purpleStyle = StyleConfig.builder()
                    .pane(PaneStyle.builder()
                            .backgroundColor("#1e1b4b")
                            .build())
                    .defaultNode(NodeStyle.builder()
                            .backgroundColor("#8b5cf6")
                            .color("#ffffff")
                            .border("2px solid #7c3aed")
                            .borderRadius(10)
                            .fontSize(14)
                            .padding(14)
                            .build())
                    .defaultEdge(EdgeStyle.builder()
                            .stroke("#a78bfa")
                            .strokeWidth(2)
                            .animated(true)
                            .type("bezier")
                            .build())
                    .build();

            Template purpleTemplate = Template.builder()
                    .name("Dark Math - Purple")
                    .styleConfig(purpleStyle)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            templateRepository.save(purpleTemplate);
            System.out.println(" Created Template: Dark Math - Purple");
        } else {
            System.out.println("️  Template 'Dark Math - Purple' already exists - skipping");
        }

        // Template 4: Minimal Math Template (Gray Theme)
        if (templateRepository.findByName("Minimal Math - Gray").isEmpty()) {
            StyleConfig grayStyle = StyleConfig.builder()
                    .pane(PaneStyle.builder()
                            .backgroundColor("#ffffff")
                            .build())
                    .defaultNode(NodeStyle.builder()
                            .backgroundColor("#6b7280")
                            .color("#ffffff")
                            .border("1px solid #4b5563")
                            .borderRadius(6)
                            .fontSize(13)
                            .padding(10)
                            .build())
                    .defaultEdge(EdgeStyle.builder()
                            .stroke("#9ca3af")
                            .strokeWidth(1)
                            .animated(false)
                            .type("step")
                            .build())
                    .build();

            Template grayTemplate = Template.builder()
                    .name("Minimal Math - Gray")
                    .styleConfig(grayStyle)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            templateRepository.save(grayTemplate);
            System.out.println(" Created Template: Minimal Math - Gray");
        } else {
            System.out.println("  Template 'Minimal Math - Gray' already exists - skipping");
        }
    }

    /**
     * Tạo hoặc cập nhật API key cho user.
     * Cho phép nhiều user dùng chung một key_value.
     * Mỗi user sẽ có bản ghi riêng để theo dõi remaining_calls.
     */
    private void createOrUpdateApiKey(User user, Packages pkg, String keyValue,
                                       int remainingCalls, int expiryDays, String userName) {
        // Kiểm tra user đã có API key chưa (theo user_id)
        Optional<ApiKey> existingUserKey = apiKeyRepository.findAll().stream()
                .filter(k -> k.getUser() != null && k.getUser().getId().equals(user.getId()))
                .findFirst();

        if (existingUserKey.isPresent()) {
            // User đã có key -> chỉ log, không cập nhật để giữ nguyên remaining_calls hiện tại
            ApiKey apiKey = existingUserKey.get();
            System.out.println("  API Key for " + userName + " already exists - Current Remaining Calls: " + apiKey.getRemainingCalls() + " - skipping update");
        } else {
            // Tạo mới - cho phép key_value trùng với user khác
            ApiKey apiKey = ApiKey.builder()
                    .user(user)
                    .aPackage(pkg)
                    .keyValue(keyValue)
                    .remainingCalls(remainingCalls)
                    .isActive(true)
                    .activatedAt(Instant.now())
                    .expiredAt(Instant.now().plus(expiryDays, ChronoUnit.DAYS))
                    .build();
            apiKeyRepository.save(apiKey);
            System.out.println(" Created API Key for " + userName + " - Remaining Calls: " + remainingCalls);
        }
    }
}
