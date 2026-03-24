package com.recipe_maker.backend.roles;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Data Transfer Object reprsenting a role for a user. */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RoleDTO {
    /** The name of the role. */
    @NotNull
    private RoleName name;
}
