package com.sbaproject.sbamindmap.config;

import com.sbaproject.sbamindmap.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Cho phép @PreAuthorize trong controller
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final UserDetailsService userDetailsService;

    // 🔐 Mã hoá mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔑 AuthenticationProvider - kết nối UserDetailsService và PasswordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        System.out.println(">>> AuthenticationProvider configured with UserDetailsService and PasswordEncoder");
        return provider;
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

                // Enable CORS với configuration từ WebConfig
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Tắt các header bảo mật có thể gây vấn đề
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                        .xssProtection(xss -> xss.disable())
                )

                // Stateless session (JWT không cần session)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Quy định quyền truy cập
                .authorizeHttpRequests(auth -> auth
                        // ✅ Cho phép truy cập không cần token
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",       // swagger docs
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/payment/vnpay/callback",
                                "/api/packages/all"      // Allow public access to packages
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
                )

                // Exception handling
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println(">>> Auth Entry Point - Path: " + request.getRequestURI());
                            System.out.println(">>> Auth Entry Point - Exception: " + authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                );

        // Thêm JWT filter vào trước UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


