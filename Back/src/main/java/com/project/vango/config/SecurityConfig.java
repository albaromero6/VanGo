package com.project.vango.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Desactivo CSRF para pruebas
            .authorizeHttpRequests()
                .anyRequest().permitAll(); // Permito todo sin autenticación por ahora

        return http.build();
    }
}
