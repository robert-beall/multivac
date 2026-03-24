package com.recipe_maker.backend.components;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.recipe_maker.backend.roles.Role;
import com.recipe_maker.backend.roles.RoleName;
import com.recipe_maker.backend.roles.RoleRepository;

/**
 * Component class for populating the role table in the database if
 * necessary roles do not already exist.
 */
@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    /** Repository for managing Role data. */
    private RoleRepository roleRepository; 

    /**
     * Constructor for role seeder class.
     * @param roleRepository
     */
    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Adds pre-defined roles to the database if they don not already exist.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // For each role name defined in RoleName enum
        for (RoleName roleName : RoleName.values()) {
            // If the role name does not exist yet, create it
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }
    
}
