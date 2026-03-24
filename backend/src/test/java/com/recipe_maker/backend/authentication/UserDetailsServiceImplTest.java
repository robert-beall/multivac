package com.recipe_maker.backend.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.recipe_maker.backend.users.User;
import com.recipe_maker.backend.users.UserRepository;
import com.recipe_maker.backend.users.UserTestUtils;

/**
 * Test class for the UserDetailsService Implementation class.
 */
public class UserDetailsServiceImplTest {
    /** The UserDtailsServiceImpl class to test. */
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    /** Mocked userRepository for testing. */
    @Mock
    private UserRepository userRepository;

    /** Test utility class for generating User test data. */
    private UserTestUtils userTestUtils;

    /** AutoCloseable instance for managing mock resources. */
    private AutoCloseable mockCloseable;

    /**
     * Constructor for instantiating utility classes.
     */
    public UserDetailsServiceImplTest() {
        userTestUtils = new UserTestUtils();
    }

    /**
     * Setup mocks.
     */
    @BeforeEach
    void setup() {
        mockCloseable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Teardown mocks.
     * @throws Exception
     */
    @AfterEach
    void teardown() throws Exception {
        mockCloseable.close();
    }

    /**
     * Test loadUserByUsername() method on successful run.
     */
    @Test   
    void testLoadUserByUsername() {
        User user = userTestUtils.createEntity();

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getUsername());

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    /**
     * Test loadUserByUsername() method when the username is not found.
     */
    @Test   
    void testLoadUserByUsernameNotFound() {
        User user = userTestUtils.createEntity();

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername(user.getUsername()));
    }
}
