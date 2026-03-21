package com.recipe_maker.backend.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the ModelMapper bean.
 */
@Configuration
public class ModelMapperConfig {
    /**
     * Defines a bean for ModelMapper, which is used for mapping between DTOs and entities.
     * @return ModelMapper instance
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
