package com.recipe_maker.backend.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for the SecurityFilterChain.
 */
@Configuration
public class SecurityFilterChainConfig {
    /**
     * Defines a bean for the security filter chain, which configures HTTP security settings.
     * @param http
     * @return SecurityFilterChain instance
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF protection for stateless APIs
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Set session management to stateless
        return http.build();
    }
}
