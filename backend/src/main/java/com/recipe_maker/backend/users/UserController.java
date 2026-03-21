package com.recipe_maker.backend.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
