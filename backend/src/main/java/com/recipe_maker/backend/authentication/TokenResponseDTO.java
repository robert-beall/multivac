package com.recipe_maker.backend.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for token responses. This class encapsulates the access token
 * that is generated and returned to the client after a successful authentication. It 
 * is used to transfer token data from the server to the client during the authentication 
 * process.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class TokenResponseDTO {
    /** The access token generated for the authenticated user. */
    private String accessToken;
    
    /** The refresh token generated for the authenticated user. */
    private String refreshToken;
}
