package com.sbaproject.sbamindmap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Cho phép origin từ frontend (React dev server)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",    // React default port
                "http://localhost:5173",    // Vite default port
                "http://127.0.0.1:3000",
                "http://127.0.0.1:5173"
        ));

        // Cho phép tất cả HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Cho phép tất cả headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Cho phép credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);

        // Áp dụng cho tất cả endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        System.out.println(">>> CORS Configuration loaded - Allowed origins: " + configuration.getAllowedOrigins());

        return source;
    }
}
