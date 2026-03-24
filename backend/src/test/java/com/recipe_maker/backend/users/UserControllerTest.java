package com.recipe_maker.backend.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.recipe_maker.backend.BaseControllerTest;
import com.recipe_maker.backend.annotations.WebMvcTestSecured;
import com.recipe_maker.backend.authentication.AuthenticationTestUtils;
import com.recipe_maker.backend.authentication.LoginDTO;
import com.recipe_maker.backend.authentication.RefreshTokenDTO;
import com.recipe_maker.backend.exceptions.ErrorMessage;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * Tests for the UserController class, which handles user-related HTTP requests.
 */
@WebMvcTestSecured(controllers = UserController.class)
public class UserControllerTest extends BaseControllerTest {
    /** The MockMvc instance for testing the controller. */
    @Autowired
    private MockMvc mockMvc;

    /** The UserService instance for testing. */
    @MockitoBean
    private UserService userService;

    /** The UserRepository instance for testing. */
    @MockitoBean
    private UserRepository userRepository;

    /** The ObjectMapper instance for testing. */
    @Autowired
    private ObjectMapper objectMapper;

    /** The utility class for creating test user data. */
    private UserTestUtils userTestUtils;

    /** The utility class for creating test authentication data. */
    private AuthenticationTestUtils authenticationTestUtils;

    /** 
     * Constructor for the UserControllerTest class, which initializes the UserTestUtils instance.
     */
    public UserControllerTest() {
        userTestUtils = new UserTestUtils();
        authenticationTestUtils = new AuthenticationTestUtils();
    }

    /**
     * Test for the register endpoint of the UserController class, 
     * which tests the successful registration of a user.
     * 
     * @throws Exception
     */
    @Test
    public void testRegisterUser() throws Exception {
        // Create a UserDTO object with test data
        UserDTO input = userTestUtils.createDTO();

        // Create a User object with the same data as the UserDTO object
        mockMvc.perform(post("/users/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated());
    }

    /**
     * Test for the register endpoint of the UserController class, 
     * which tests the registration of a user with a username that 
     * already exists, resulting in a 409 Conflict error.
     * 
     * @throws Exception
     */
    @Test
    public void testRegisterUser409Conflict() throws Exception {
        // Create a UserDTO object with test data
        UserDTO input = userTestUtils.createDTO();

        // Throw a DataIntegrityViolationException when the registerUser method is called,
        doThrow(new DataIntegrityViolationException("Username already exists"))
            .when(userService)
            .registerUser(any());

        // Perform a POST request to the /users/register endpoint with the UserDTO object as the request body
        MockHttpServletResponse response = mockMvc.perform(post("/users/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();
        
        // Assert that the response status is 409 Conflict
        assertEquals(409, response.getStatus());

        // Assert that the error message in the response body is "Username already exists"
        ErrorMessage errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorMessage.class);
        assertEquals("Conflict", errorResponse.getError());
        assertEquals("Username already exists", errorResponse.getMessage()); 
    }

    /**
     * Test for the register endpoint of the UserController class, 
     * which tests the registration of a user when an unexpected error occurs,
     * resulting in a 500 Internal Server Error.
     * 
     * @throws Exception
     */
    @Test
    public void testRegisterUser500InternalServerError() throws Exception {
        // Create a UserDTO object with test data
        UserDTO input = userTestUtils.createDTO();

        // Throw a RuntimeException when the registerUser method is called, simulating an unexpected error
        doThrow(new RuntimeException("Internal server error"))
            .when(userService)
            .registerUser(any());

        // Perform a POST request to the /users/register endpoint with the UserDTO object as the request body
        MockHttpServletResponse response = mockMvc.perform(post("/users/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();
        
        // Assert that the response status is 500 Internal Server Error
        assertEquals(500, response.getStatus());  
        
        // Assert that the error message in the response body is "An unexpected error occurred"
        ErrorMessage errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorMessage.class);
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An unexpected error occurred", errorResponse.getMessage()); 
    }

    /**
     * Test for the login endpoint of the UserController class, 
     * which tests the successful login of a user.
     * @throws JacksonException
     * @throws Exception
     */
    @Test
    public void testLogin() throws JacksonException, Exception {
        LoginDTO input = authenticationTestUtils.createLoginDTO();

        // Perform a POST request to the /users/login endpoint with the LoginDTO object as the request body
        mockMvc.perform(post("/users/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    /**
     * Test for the login endpoint of the UserController class, 
     * which tests the login of a user with a disabled account, resulting in a 403 Forbidden error.
     * @throws JacksonException
     * @throws Exception
     */
    @Test
    public void testLoginDisabled() throws JacksonException, Exception {
        LoginDTO input = authenticationTestUtils.createLoginDTO();

        when(userService.login(any())).thenThrow(new DisabledException("Your account has been disabled. Please contact support for assistance."));

        // Perform a POST request to the /users/login endpoint with the LoginDTO object as the request body
        MockHttpServletResponse response = mockMvc.perform(post("/users/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();
        
        // Assert that the response status is 403 Forbidden
        assertEquals(403, response.getStatus());
        // Assert that the error message in the response body is "Your account has been disabled. Please contact support for assistance."
        ErrorMessage errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorMessage.class);
        assertEquals("Forbidden", errorResponse.getError());
        assertEquals("Your account has been disabled. Please contact support for assistance.", errorResponse.getMessage());
    }

    /**
     * Test for the login endpoint of the UserController class, 
     * which tests the login of a user with a locked account, resulting in a 403 Forbidden error.
     * @throws JacksonException
     * @throws Exception
     */
    @Test
    public void testLoginLocked() throws JacksonException, Exception {
        LoginDTO input = authenticationTestUtils.createLoginDTO();

        when(userService.login(any())).thenThrow(new LockedException("Your account has been locked. Please contact support for assistance."));

        // Perform a POST request to the /users/login endpoint with the LoginDTO object as the request body
        MockHttpServletResponse response = mockMvc.perform(post("/users/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();

        // Assert that the response status is 403 Forbidden
        assertEquals(403, response.getStatus());
        // Assert that the error message in the response body is "Your account has been locked. Please contact support for assistance."
        ErrorMessage errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorMessage.class);
        assertEquals("Forbidden", errorResponse.getError());
        assertEquals("Your account has been locked. Please contact support for assistance.", errorResponse.getMessage());
    }

    /**
     * Test for the login endpoint of the UserController class, 
     * which tests the login of a user with a locked account, resulting in a 403 Forbidden error.
     * @throws JacksonException
     * @throws Exception
     */
    @Test
    public void testLoginBadCredentials() throws JacksonException, Exception {
        LoginDTO input = authenticationTestUtils.createLoginDTO();

        when(userService.login(any())).thenThrow(new BadCredentialsException("Invalid username or password."));

        // Perform a POST request to the /users/login endpoint with the LoginDTO object as the request body
        MockHttpServletResponse response = mockMvc.perform(post("/users/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andReturn()
                .getResponse();

        // Assert that the response status is 403 Forbidden
        assertEquals(403, response.getStatus());
        // Assert that the error message in the response body is "Invalid username or password."
        ErrorMessage errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorMessage.class);
        assertEquals("Forbidden", errorResponse.getError());
        assertEquals("Invalid username or password.", errorResponse.getMessage());
    }

    /**
     * Test for the login endpoint of the UserController class, 
     * which tests the successful login of a user.
     * @throws JacksonException
     * @throws Exception
     */
    @Test
    public void testRefresh() throws JacksonException, Exception {
        RefreshTokenDTO input = authenticationTestUtils.createRefreshTokenDTO();

        // Perform a POST request to the /users/refresh endpoint with the RefreshTokenDTO object as the request body
        mockMvc.perform(post("/users/refresh")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    /**
     * Test for the refresh endpoint of the UserController class,
     * which tests the refresh of a user's access token with an invalid 
     * refresh token, resulting in a 400 Bad Request error.
     * 
     * @throws JacksonException
     * @throws Exception
     */
    @Test
    public void testRefreshInvalidToken() throws JacksonException, Exception {
        // Create a RefreshTokenDTO object with test data
        RefreshTokenDTO input = authenticationTestUtils.createRefreshTokenDTO();

        // Throw an IllegalArgumentException when the refresh method is called, simulating an invalid refresh token
        when(userService.refresh(any())).thenThrow(new IllegalArgumentException("Invalid refresh token"));

        // Perform a POST request to the /users/refresh endpoint with the RefreshTokenDTO object as the request body
        MockHttpServletResponse response = mockMvc.perform(post("/users/refresh")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andReturn().
                getResponse();

        // Assert that the response status is 400 Bad Request
        assertEquals(400, response.getStatus());

        // Assert that the error message in the response body is "Invalid refresh token"
        ErrorMessage errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorMessage.class);
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Invalid refresh token", errorResponse.getMessage());
    }

    /**
     * Test for the logout endpoint of the UserController class, 
     * which tests the successful logout of a user.
     * @throws JacksonException
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void testLogout() throws JacksonException, Exception {
        // Perform a POST request to the /users/logout endpoint 
        mockMvc.perform(post("/users/logout")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    /**
     * Test for the logout endpoint of the UserController class, 
     * which tests the successful logout of a user when the username
     * is not found. 
     * 
     * @throws JacksonException
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void testLogoutUnauthorized() throws JacksonException, Exception {
       doThrow(UsernameNotFoundException.class).when(userService).logout();

        // Perform a POST request to the /users/logout endpoint 
        MockHttpServletResponse response = mockMvc.perform(post("/users/logout")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        
        // Assert that the response status is 401 Unauthorized
        assertEquals(401, response.getStatus());

        // Assert that the error message in the response body is "User not found"
        ErrorMessage errorResponse = objectMapper.readValue(response.getContentAsString(), ErrorMessage.class);
        assertEquals("Unauthorized", errorResponse.getError());
        assertEquals("User not found", errorResponse.getMessage());
    }
}
