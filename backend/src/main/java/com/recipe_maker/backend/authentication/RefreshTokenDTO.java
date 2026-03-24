package com.recipe_maker.backend.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class representing refresh token.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RefreshTokenDTO {
    /** The refresh token generated for the authenticated user. */
    private String refreshToken;   
}
