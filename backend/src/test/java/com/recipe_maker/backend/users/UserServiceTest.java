package com.recipe_maker.backend.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Test class for UserService, which manages user-related operations such as registration.
 */
public class UserServiceTest {
    /** The service for managing user-related operations. */
    @InjectMocks
    private UserService userService;

    /** The repository for managing user data. */
    @Mock 
    private UserRepository userRepository;

    /** The ModelMapper for mapping between DTOs and entities. */
    @Mock
    private ModelMapper modelMapper;

    /** The PasswordEncoder for encoding user passwords. */
    @Mock
    private PasswordEncoder passwordEncoder;

    /** The utility class for creating test user data. */
    private UserTestUtils userTestUtils;

    /** The auto-closeable resource for managing mock objects. */
    private AutoCloseable autoCloseable;

    /** Constructor for initializing the test class. */
    public UserServiceTest() {
        userTestUtils = new UserTestUtils();
    }

    /** Sets up the test environment before each test method is executed. */
    @BeforeEach
    void setUp() {
        autoCloseable = org.mockito.MockitoAnnotations.openMocks(this);
    }

    /** Tears down the test environment after each test method is executed. */
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    /**
     * Tests the existsByUsername method of UserService to ensure that it correctly identifies whether a user with a given username exists.
     */
    @Test
    void testExistsByUsername() {
        // Create a test user and set up the mock repository to return true for the user's username
        User user = userTestUtils.createEntity();
        String username = user.getUsername();
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Assert that the existsByUsername method returns true for the given username
        assertTrue(userService.existsByUsername(username));
    }

    /**
     * Tests the existsByUsername method of UserService to ensure that it correctly identifies whether a user with a given username does not exist.
     */
    @Test
    void testExistsByUsernameNotFound() {
        // Create a test user and set up the mock repository to return false for the user's username
        User user = userTestUtils.createEntity();
        String username = user.getUsername();
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // Assert that the existsByUsername method returns false for the given username
        assertFalse(userService.existsByUsername(username));
    }

    /**
     * Tests the existsByEmail method of UserService to ensure that it correctly identifies whether a user with a given email exists.
     */
    @Test
    void testExistsByEmail() {
        // Create a test user and set up the mock repository to return true for the user's email
        User user = userTestUtils.createEntity();
        String email = user.getEmail();
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Assert that the existsByEmail method returns true for the given email
        assertTrue(userService.existsByEmail(email));
    }

    /**
     * Tests the existsByEmail method of UserService to ensure that it correctly identifies whether a user with a given email does not exist.
     */
    @Test
    void testExistsByEmailNotFound() {
        // Create a test user and set up the mock repository to return false for the user's email
        User user = userTestUtils.createEntity();
        String email = user.getEmail();
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // Assert that the existsByEmail method returns false for the given email
        assertFalse(userService.existsByEmail(email));
    }

    /**
     * Tests the registerUser method of UserService to ensure that a user is registered correctly when valid data is provided.
      * It verifies that the userRepository's save method is called with the expected User entity.
      * It also checks that the appropriate methods are called on the modelMapper and passwordEncoder mocks.
      * @throws DataIntegrityViolationException if a user with the same username or email already exists
     */
    @Test
    void testRegisterUser() {
        UserDTO dto = userTestUtils.createDTO();

        User expected = new User();
        expected.setUsername(dto.getUsername());
        expected.setPassword(dto.getPassword());
        expected.setEmail(dto.getEmail());

        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(expected);
        when(modelMapper.map(dto, User.class)).thenReturn(expected);
        when(modelMapper.map(expected, UserDTO.class)).thenReturn(dto);
        when(passwordEncoder.encode(any())).thenReturn(dto.getPassword());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    
        userService.registerUser(dto);
        verify(userRepository, times(1)).save(userCaptor.capture());
        assertEquals(expected, userCaptor.getValue());
    }

    /**
     * Tests the registerUser method of UserService to ensure that a DataIntegrityViolationException is thrown when a user with the same username already exists.
      * It verifies that the userRepository's save method is not called when a duplicate username is detected.
      * It also checks that the appropriate methods are called on the modelMapper and passwordEncoder mocks.
      * @throws DataIntegrityViolationException if a user with the same username already exists
     */
    @Test
    void testRegisterUserDuplicateUsername() {
        UserDTO dto = userTestUtils.createDTO();
        
        User expected = new User();
        expected.setUsername(dto.getUsername());
        expected.setPassword(dto.getPassword());
        expected.setEmail(dto.getEmail());

        when(userRepository.existsByUsername(any())).thenReturn(true);
        when(modelMapper.map(dto, User.class)).thenReturn(expected);
        when(modelMapper.map(expected, UserDTO.class)).thenReturn(dto);
        when(passwordEncoder.encode(any())).thenReturn(dto.getPassword());

    
        assertThrows(DataIntegrityViolationException.class, () -> userService.registerUser(dto), "Username already exists");
        verify(userRepository, times(0)).save(any(User.class));
    }

    /**
     * Tests the registerUser method of UserService to ensure that a DataIntegrityViolationException is thrown when a user with the same email already exists.
      * It verifies that the userRepository's save method is not called when a duplicate email is detected.
      * It also checks that the appropriate methods are called on the modelMapper and passwordEncoder mocks.
      * @throws DataIntegrityViolationException if a user with the same email already exists
     */
    @Test
    void testRegisterUserDuplicateEmail() {
        UserDTO dto = userTestUtils.createDTO();
        
        User expected = new User();
        expected.setUsername(dto.getUsername());
        expected.setPassword(dto.getPassword());
        expected.setEmail(dto.getEmail());
        
        when(userRepository.existsByEmail(any())).thenReturn(true);
        when(modelMapper.map(dto, User.class)).thenReturn(expected);
        when(modelMapper.map(expected, UserDTO.class)).thenReturn(dto);
        when(passwordEncoder.encode(any())).thenReturn(dto.getPassword());

    
        assertThrows(DataIntegrityViolationException.class, () -> userService.registerUser(dto));
        verify(userRepository, times(0)).save(any(User.class));
    }
}
