package com.dnd.backend.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200"); // Zezwól na frontend
        config.addAllowedHeader("*"); // Zezwól na wszystkie nagłówki
        config.addAllowedMethod("*"); // Zezwól na wszystkie metody (GET, POST, itd.)
        config.setAllowCredentials(true); // Zezwól na uwierzytelnianie ciasteczkami

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Dotyczy wszystkich endpointów
        return new CorsFilter(source);
    }
}
