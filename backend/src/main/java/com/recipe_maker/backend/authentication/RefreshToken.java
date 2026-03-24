package com.recipe_maker.backend.authentication;

import java.time.Instant;

import com.recipe_maker.backend.users.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** A class representing a refresh token for user authentication. */
@AllArgsConstructor 
@Data 
@Entity
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {
    /** The unique identifier for the refresh token. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The token string for the refresh token. */
    @Column(nullable = false, unique = true)
    private String token;

    /** The user associated with the refresh token. */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** The expiration time of the refresh token. */
    @Column(nullable = false)
    private Instant expiresAt;

    /**
     * Checks if the refresh token has expired.
     * @return true if the refresh token has expired, false otherwise
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}