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

    // 🔰 Cấu hình bảo mật - MỞ HOÀN TOÀN CHO TEST
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF
                .csrf(csrf -> csrf.disable())

                // Enable CORS với configuration từ WebConfig
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Tắt các header bảo mật có thể gây vấn đề
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                        .xssProtection(xss -> xss.disable())
                )

                // Stateless session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // ============ PUBLIC APIs - Không cần đăng nhập ============
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/payment/vnpay/callback").permitAll()

                        // ============ API KEYS - USER & ADMIN ============
                        .requestMatchers("/api/keys/available").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/keys/{id}/check").hasAnyRole("USER", "ADMIN")

                        // ============ GENERATED DATA ============
                        .requestMatchers("GET", "/api/generated-data/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/generated-data/**").hasRole("ADMIN")

                        // ============ TEMPLATES ============
                        .requestMatchers("GET", "/api/templates/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/templates/**").hasRole("ADMIN")

                        // ============ MINDMAPS ============
                        .requestMatchers("GET", "/api/mindmaps/{id}/detail").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("GET", "/api/mindmaps/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("GET", "/api/mindmaps").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("POST", "/api/mindmaps/from-generated-data").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("PUT", "/api/mindmaps/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("POST", "/api/mindmaps").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("DELETE", "/api/mindmaps/{id}").hasRole("ADMIN")

                        // ============ CHAT & CONVERSATIONS ============
                        .requestMatchers("/api/chat/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/conversations/**").hasAnyRole("USER", "ADMIN")

                        // ============ CHATGPT & GEMINI (Test APIs) ============
                        .requestMatchers("/api/chatgpt/**").hasRole("ADMIN")
                        .requestMatchers("/api/gemini/**").hasRole("ADMIN")

                        // ============ ADMIN-ONLY APIs ============
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // ============ TẤT CẢ CÒN LẠI CẦN AUTHENTICATED ============
                        .anyRequest().authenticated()
                )

                // Exception handling
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println(">>> Auth Entry Point - Path: " + request.getRequestURI());
                            System.out.println(">>> Auth Entry Point - Exception: " + authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                )

                // Thêm JWT Filter trước UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
