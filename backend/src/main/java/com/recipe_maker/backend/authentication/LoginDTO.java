package com.recipe_maker.backend.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for login requests. This class encapsulates the username and password
 * provided by the user when attempting to log in. It is used to transfer login data from
 * the client to the server during the authentication process.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginDTO {
    /** The username of the user attempting to log in. */
    private String username;

    /** The password of the user attempting to log in. */
    private String password;
}
