package com.recipe_maker.backend.authentication;

import java.time.Instant;

import com.recipe_maker.backend.users.UserTestUtils;

import net.datafaker.Faker;

/**
 * Utility class for creating test data related to authentication, such as LoginDTO and TokenResponseDTO.
 */
public class AuthenticationTestUtils {
    /** The Faker instance for generating random test data. */
    private Faker faker;

    /** UserTestUtils for creating test data. */
    private UserTestUtils userTestUtils;

    /**
     * Constructs an instance of {@code AuthorizationTestUtils}.
     */
    public AuthenticationTestUtils() {
        this.faker = new Faker();
        this.userTestUtils = new UserTestUtils();
    }

    /**
     * Creates a {@code LoginDTO} with random username and password for testing purposes.
     * @return a {@code LoginDTO} with random credentials
     */
    public LoginDTO createLoginDTO() {
        return new LoginDTO(faker.credentials().username(), faker.credentials().password());
    }

    /**
     * Creates a {@code TokenResponseDTO} with random access and refresh tokens for testing purposes.
     * @return a {@code TokenResponseDTO} with random tokens
     */
    public TokenResponseDTO createTokenResponseDTO() {
        return new TokenResponseDTO(faker.internet().uuid(), faker.internet().uuid());
    }

    /**
     * Creates a {@code RefreshTokenDTO} with a random refresh token for testing purposes.
     * @return a {@code RefreshTokenDTO} with a random refresh token
     */
    public RefreshTokenDTO createRefreshTokenDTO() {
        return new RefreshTokenDTO(faker.internet().uuid());
    }

    /**
     * Creates a {@code RefreshToken} with a random token string and 
     * expiration time for testing purposes.
     *  
     * @return a {@code RefreshToken} with a random token and expiration time
     */
    public RefreshToken createRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userTestUtils.createEntity());
        refreshToken.setToken(faker.internet().uuid());
        refreshToken.setExpiresAt(faker.timeAndDate().future(10, java.util.concurrent.TimeUnit.DAYS));
        return refreshToken;
    }

    /**
     * Return a RefreshToken instance guaranteed to be expired. 
     * @return RefreshToken with expired value
     */
    public RefreshToken createExpiredRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userTestUtils.createEntity());
        refreshToken.setToken(faker.internet().uuid());
        refreshToken.setExpiresAt(Instant.now());
        return refreshToken;
    }
}
