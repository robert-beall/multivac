package com.recipe_maker.backend.authentication;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import net.datafaker.Faker;

/**
 * Test class for JwtService class.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JwtService.class)
@TestPropertySource(locations = "classpath:application.properties")
public class JwtServiceTest {
    /** The JwtService to test. */
    @Autowired
    private JwtService jwtService;

    /** Faker instance from generating test data. */
    private Faker faker; 

    /** Access token expiration test value. */
    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    /** Refresh token expiration test value. */
    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    /**
     * Constructor for test class.
     */
    public JwtServiceTest() {
        faker = new Faker();
    }

    /**
     * Test getSigninKey() method when successful.
     */
    @Test
    void testGetSigninKey() {
        // generating a key should return a non-null value and not through exceptions.
        assertDoesNotThrow(() -> {
            SecretKey secretKey = jwtService.getSigningKey();
            assertNotNull(secretKey);
        });
    }

    /**
     * Test generateAccessToken() method when successful.
     */
    @Test
    void testGenerateAccessToken() {
        // Generate username and expiration
        String username = faker.credentials().username();

        // Assert that token generation does not throw exception
        assertDoesNotThrow(() -> {
            // Generate and store token
            String token = jwtService.generateAccessToken(username);

            // Assert that the token is not null
            assertNotNull(token);
        });
    }

    /**
     * Test generateRefreshToken() method when successful.
     */
    @Test
    void testGenerateRefreshToken() {
        // Generate username and expiration
        String username = faker.credentials().username();

        // Assert that token generation does not throw exception
        assertDoesNotThrow(() -> {
            // Generate and store token
            String token = jwtService.generateRefreshToken(username);

            // Assert that the token is not null
            assertNotNull(token);
        });
    }

    /**
     * Test generateToken() method when successful.
     */
    @Test
    void testGenerateToken() {
        // Generate username and expiration
        String username = faker.credentials().username();
        Long expiration = faker.number().randomNumber();

        // Assert that token generation does not throw exception
        assertDoesNotThrow(() -> {
            // Generate and store token
            String token = jwtService.generateToken(username, expiration);

            // Assert that the token is not null
            assertNotNull(token);
        });
    }

    /**
     * Test extractUsername() method.
     */
    @Test
    void testExtractUsername() {
        // Generate test username
        String expected = faker.credentials().username();

        // Create a token from username
        String token = jwtService.generateAccessToken(expected);

        // Store method results
        String actual = jwtService.extractUsername(token);

        // Assert results match expected value
        assertEquals(expected, actual);
    }

    /**
     * Test isTokenValid() method when successful.
     */
    @Test
    void testIsTokenValid() {
        // Generate username and expiration
        String username = faker.credentials().username();

        String token = jwtService.generateToken(username, refreshTokenExpiration);

        assertTrue(jwtService.isTokenValid(token));
    }
    
    /**
     * Test isTokenValid() method when validation fails.
     */
    @Test
    void testIsTokenValueInvalid() {
        assertFalse(jwtService.isTokenValid(faker.internet().uuid()));
    }

    /**
     * Test getAccessTokenExpiration() method with test values.
     */
    @Test
    void testGetAccessTokenExpiration() {
        // Call service method for value
        long results = jwtService.getAccessTokenExpiration();

        // Assert the result matches test properties
        assertEquals(accessTokenExpiration, results);
    }

    /**
     * Test getRefreshTokenExpiration() method with test values.
     */
    @Test
    void testGetRefreshTokenExpiration() {
        // Call service method for value
        long results = jwtService.getRefreshTokenExpiration();

        // Assert the result matches test properties
        assertEquals(refreshTokenExpiration, results);
    }
}
