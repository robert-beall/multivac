package com.recipe_maker.backend.authentication;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe_maker.backend.users.User;

/**
 * A repository for managing refresh tokens.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    /**
     * Finds a refresh token by its token string.
     * @param token the token string to search for
     * @return an Optional containing the found RefreshToken, or empty if not found
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Deletes refresh tokens associated with a specific user.
     * @param user the user whose refresh tokens should be deleted
     */
    void deleteByUser(User user);
}
