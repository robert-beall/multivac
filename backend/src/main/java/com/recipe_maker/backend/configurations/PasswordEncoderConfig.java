package com.recipe_maker.backend.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Password Encoder configuration class for the application.
 * This class defines the PasswordEncoder bean.
 */
@Configuration
public class PasswordEncoderConfig {
    /**
     * Defines a bean for password encoding using BCrypt.
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }   
}
