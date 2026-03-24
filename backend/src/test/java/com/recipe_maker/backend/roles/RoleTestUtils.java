package com.recipe_maker.backend.roles;

import java.util.List;

import com.recipe_maker.backend.TestUtils;

import net.datafaker.Faker;

public class RoleTestUtils implements TestUtils<Role, RoleDTO> {
    /** The Faker instance for generating test data. */
    private Faker faker; 

    /**
     * Constructor for test utils class.
     */
    public RoleTestUtils() {
        faker = new Faker();
    }

    /**
     * Creates an instance of the Role entity for testing purposes.
     * @return Rol instance
     */
    public Role createEntity() {
        // Create test data values
        Long id = faker.number().randomNumber();
        RoleName name = faker.options().option(RoleName.class);

        // Return new role instance
        return new Role(id, name);
    }

    /**
     * Creates a list of Role entities for testing purposes.
     * @param size the number of Role entities to create
     * @return List of Role instances
     */
    public List<Role> createEntityList(int size) {
        return faker.collection(() -> createEntity()).len(size).generate();
    }

    /**
     * Creates an instance of the RoleDTO for testing purposes.
     * @return RoleDTO instance
     */
    public RoleDTO createDTO() {
        // Create test data values
        RoleName name = faker.options().option(RoleName.class);

        // Return new role dto instance
        return new RoleDTO(name);
    }

    /**
     * Creates a list of RoleDTO instances for testing purposes.
     * @param size the number of RoleDTO instances to create
     * @return List of RoleDTO instances
     */
    public List<RoleDTO> createDTOList(int size) {
        return faker.collection(() -> createDTO()).len(size).generate();
    }
}
