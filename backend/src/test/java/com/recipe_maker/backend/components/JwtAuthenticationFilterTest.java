package com.recipe_maker.backend.components;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.recipe_maker.backend.authentication.JwtService;
import com.recipe_maker.backend.users.User;
import com.recipe_maker.backend.users.UserTestUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.datafaker.Faker;

/**
 * Test class for JwtAuthenticationFilter.
 */
public class JwtAuthenticationFilterTest {
    /** JwtAuthenticationFilter class to test. */
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /** Mocked request. */
    @Mock
    private HttpServletRequest request;

    /** Mocked response. */
    @Mock
    private HttpServletResponse response; 

    /** Mocked jwtService. */
    @Mock
    private JwtService jwtService; 

    /** Mocked UserDetailsService. */
    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    /** Faker to generate test data. */
    private Faker faker; 

    private UserTestUtils userTestUtils;

    /** AutoCloseable for managing mocked resources. */
    private AutoCloseable mockCloseable; 

    /**
     * Constructor for test class
     */
    public JwtAuthenticationFilterTest() {
        faker = new Faker();
        userTestUtils = new UserTestUtils();
    }

    /**
     * Open mocks before each test.
     */
    @BeforeEach
    void setup() {
        mockCloseable = MockitoAnnotations.openMocks(this);
    } 

    /**
     * Close mock rsources after each test.
     * @throws Exception
     */
    @AfterEach
    void teardown() throws Exception {
        mockCloseable.close();
    }

    /**
     * Test doFilterInternal() method on successful run.
     * @throws ServletException 
     * @throws IOException 
     */
    @Test
    void testDoFilterInternal() throws IOException, ServletException {
        String header = "Bearer " + faker.internet().uuid();
        User user = userTestUtils.createEntity();

        when(request.getHeader("Authorization")).thenReturn(header);
        when(jwtService.isTokenValid(any())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(user);

        assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        verify(filterChain, times(1)).doFilter(request, response);
    }

    /**
     * Test doFilterInternal() method with null header.
     * @throws ServletException 
     * @throws IOException 
     */
    @Test
    void testDoFilterInternalNullHeader() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);

        assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

        verify(filterChain, times(1)).doFilter(request, response);
    }

    /**
     * Test doFilterInternal() method with non-bearer header.
     * @throws ServletException 
     * @throws IOException 
     */
    @Test
    void testDoFilterInternalNonBearerHeader() throws IOException, ServletException {
        String header = faker.internet().uuid();
        
        when(request.getHeader("Authorization")).thenReturn(header);

        assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

        verify(filterChain, times(1)).doFilter(request, response);
    }

    /**
     * Test doFilterInternal() method with invalid token.
     * @throws ServletException 
     * @throws IOException 
     */
    @Test
    void testDoFilterInternalInvalidToken() throws IOException, ServletException {
        String header = "Bearer " + faker.internet().uuid();
        
        when(request.getHeader("Authorization")).thenReturn(header);
        when(jwtService.isTokenValid(any())).thenReturn(false);

        assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        verify(filterChain, times(1)).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(any());
    }
}
