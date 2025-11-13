package com.sbaproject.sbamindmap.config;

import com.sbaproject.sbamindmap.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Cho phép @PreAuthorize trong controller
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // 🔐 Mã hoá mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ⚙️ AuthenticationManager (để dùng trong AuthController)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 🔰 Cấu hình bảo mật tổng thể
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF vì dùng JWT
                .csrf(csrf -> csrf.disable())

                // Stateless session (JWT không cần session)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Quy định quyền truy cập
                .authorizeHttpRequests(auth -> auth
                        // ✅ Cho phép truy cập không cần token
                        .requestMatchers(
                                "/api/auth/**",          // login
                                "/v3/api-docs/**",       // swagger docs
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/payment/vnpay/callback"
                        ).permitAll()

                        // ✅ Phân quyền theo Role (Role trong DB là int)
                        // ROLE_ADMIN -> quyền full
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // ROLE_MODERATOR -> quyền trung gian
                        .requestMatchers("/api/mod/**").hasAnyRole("ADMIN", "MODERATOR")
                        // ROLE_USER -> quyền xem cơ bản
                        .requestMatchers("/api/user/**").hasAnyRole("ADMIN", "MODERATOR", "USER")

                        // Tất cả endpoint khác cần login
                        .anyRequest().authenticated()
                );

        // Thêm JWT filter vào trước UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


