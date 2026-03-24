package com.recipe_maker.backend.components;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.event.ContextRefreshedEvent;

import com.recipe_maker.backend.roles.Role;
import com.recipe_maker.backend.roles.RoleName;
import com.recipe_maker.backend.roles.RoleRepository;

/**
 * Test class for RoleSeeder component class.
 */
public class RoleSeederTest {
    /** RoleSeeder class to test. */
    @InjectMocks
    private RoleSeeder roleSeeder;

    /** Mocked repository for managing role data. */
    @Mock
    private RoleRepository roleRepository;

    /** Mocked ContextRefreshedEvent for onApplicationEvent calls. */
    @Mock
    private ContextRefreshedEvent event;

    /** AutoCloseable for handling mocked resources. */
    private AutoCloseable mockCloseable;

    /**
     * Setup logic opens mocks.
     */
    @BeforeEach
    void setup() {
        mockCloseable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Teardown logic cleans up mocked resources.
     * @throws Exception
     */
    @AfterEach
    void teardown() throws Exception {
        mockCloseable.close();
    }

    /**
     * Test onApplicationEvent() when no roles exist in the database.
     */
    @Test
    void testOnApplicationEvent() {
        // Mock an empty database
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());

        // Assert that no exceptions are thrown during execution
        assertDoesNotThrow(() -> roleSeeder.onApplicationEvent(event));

        // The number of roles saved should correspond to the number of roles defined in the enum
        verify(roleRepository, times(RoleName.values().length)).save(any());
    }

    /**
     * Test onApplicationEvent() when all roles have already been populate in the database.
     */
    @Test
    void testOnApplicationEventAlreadyPopulated() {
        // Mock a populated database
        when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role()));

        // Assert that no exceptions are thrown during execution
        assertDoesNotThrow(() -> roleSeeder.onApplicationEvent(event));

        // No roles should be saved to the database
        verify(roleRepository, never()).save(any());
    }
}
