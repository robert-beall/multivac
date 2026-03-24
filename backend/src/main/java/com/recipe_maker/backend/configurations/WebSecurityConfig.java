package com.recipe_maker.backend.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.recipe_maker.backend.components.JwtAuthenticationFilter;

/**
 * Configuration class for the SecurityFilterChain.
 */
@Configuration
@EnableMethodSecurity // Enable method-level security annotations like @PreAuthorize and @PostAuthorize
public class WebSecurityConfig {
    /**
     * The JWT authentication filter.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructor for WebSecurityConfig.
     * @param jwtAuthenticationFilter The JWT authentication filter to be used in the security configuration.
     */
    public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

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
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Set session management to stateless
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/users/register", "/users/login", "/users/refresh").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add the JWT authentication filter before the default username/password authentication filter
        return http.build();
    }

    /**
     * Defines a bean for the AuthenticationManager, which is responsible for processing authentication requests.
     * @param config The AuthenticationConfiguration from which to obtain the AuthenticationManager.
     * @return The AuthenticationManager instance.
     * @throws Exception If an error occurs while obtaining the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
