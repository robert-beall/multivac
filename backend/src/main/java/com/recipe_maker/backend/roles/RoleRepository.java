package com.recipe_maker.backend.roles;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing role data.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Find role by name.
     * @param name
     * @return Optional of Role
     */
    Optional<Role> findByName(RoleName name);
}
