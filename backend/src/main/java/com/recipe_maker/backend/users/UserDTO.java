package com.recipe_maker.backend.users;

import java.util.Set;

import com.recipe_maker.backend.roles.RoleDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for representing a user in the system.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserDTO { 
    /** The username for the user. */
    @NotNull
    private String username;

    /** The password for the user. */
    @NotNull
    @Size(min = 8)
    private String password;

    /** The email for the user. */
    @NotNull
    @Email
    private String email;
    
    /** The roles of the user. */
    @NotEmpty
    private Set<RoleDTO> role;
}
