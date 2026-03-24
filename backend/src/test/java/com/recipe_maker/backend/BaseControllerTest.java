package com.recipe_maker.backend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.recipe_maker.backend.annotations.WebMvcTestSecured;
import com.recipe_maker.backend.components.JwtAuthenticationFilter;

import jakarta.servlet.FilterChain;

/**
 * Base test class for controller tests, which sets up common configurations 
 * and mocks for testing controllers. This class is extended by specific controller 
 * test classes to avoid code duplication and ensure consistent test setup across 
 * all controller tests.
 */
@WebMvcTestSecured
public abstract class BaseControllerTest {
    /** The JwtAuthenticationFilter instance for testing. */
    @MockitoBean
    protected JwtAuthenticationFilter jwtAuthenticationFilter;

    /** The UserDetailsService instance for testing. */
    @MockitoBean
    protected UserDetailsService userDetailsService;

    /**
     * Setup method that configures the JwtAuthenticationFilter to allow all requests to pass through
     * without performing actual authentication. This is done to focus on testing the controller logic
     * without being blocked by authentication issues.
     * @throws Exception
     */
    @BeforeEach
    void setupJwtFilter() throws Exception {
        doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(
                invocation.getArgument(0),
                invocation.getArgument(1)
            );
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }
}
