package com.recipe_maker.backend.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recipe_maker.backend.authentication.LoginDTO;
import com.recipe_maker.backend.authentication.RefreshTokenDTO;
import com.recipe_maker.backend.authentication.TokenResponseDTO;

import jakarta.validation.Valid;

/**
 * Controller class for managing user-related endpoints.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    /** The service for managing user-related operations. */
    private final UserService userService;

    /**
     * Constructor for UserController.
     * @param userService
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint for registering a new user.
     * @param userDTO
     * @return UserDTO of user to register
     */
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Endpoint for logging in a user and generating JWT tokens.
     * @param loginDTO
     * @return TokenResponseDTO containing the access and refresh tokens
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(userService.login(loginDTO));
    }

    /**
     * Endpoint for refreshing the access token using a valid refresh token.
     * @param refreshTokenDTO
     * @return TokenResponseDTO containing the new access and refresh tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        return ResponseEntity.ok(userService.refresh(refreshTokenDTO));
    }

    /**
     * Endpoint for logging out a user.
     * 
     * @return void response.
     */
    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> logout() {
        userService.logout();
        return ResponseEntity.noContent().build();
    }
}
