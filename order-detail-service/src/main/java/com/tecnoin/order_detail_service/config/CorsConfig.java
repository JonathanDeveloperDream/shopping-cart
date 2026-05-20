package com.tecnoin.order_detail_service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        // Create a new CORS configuration object
        CorsConfiguration config = new CorsConfiguration();
        // Allow requests from the frontend running on localhost:8080
        config.addAllowedOrigin("http://localhost:8080");
        // Allow all HTTP headers in requests
        config.addAllowedHeader("*");
        // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*");
        // Allow credentials such as cookies and authorization headers
        config.setAllowCredentials(true);
        // Create the source where the CORS configuration will be stored
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        // Apply this CORS configuration to all endpoints
        source.registerCorsConfiguration("/**", config);
        // Return the configured CORS filter
        return new CorsFilter(source);
    }
}