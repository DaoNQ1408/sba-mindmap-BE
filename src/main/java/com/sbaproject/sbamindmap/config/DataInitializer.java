package com.sbaproject.sbamindmap.config;

import com.sbaproject.sbamindmap.entity.PaymentMethod;
import com.sbaproject.sbamindmap.entity.User;
import com.sbaproject.sbamindmap.enums.UserRole;
import com.sbaproject.sbamindmap.enums.UserStatus;
import com.sbaproject.sbamindmap.repository.PaymentMethodRepository;
import com.sbaproject.sbamindmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public void run(String... args) {

        User u1 = new User();
        u1.setMail("admin@gmail.com");
        u1.setUsername("admin");
        u1.setPassword(passwordEncoder.encode("@1"));
        u1.setRole(UserRole.ADMIN);
        u1.setUserStatus(UserStatus.ACTIVE);

        userRepository.save(u1);

        // Seed VNPay payment method (without checking exists)
        PaymentMethod pm = new PaymentMethod();
        pm.setName("VNPAY");
        pm.setIsActive(true);
        paymentMethodRepository.save(pm);

        System.out.println("🔥 Seeded default admin + VNPAY (NO CHECK MODE)");

    }
}
