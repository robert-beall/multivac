package com.recipe_maker.backend.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.recipe_maker.backend.annotations.WebMvcTestSecured;
import com.recipe_maker.backend.exceptions.ErrorMessage;

import tools.jackson.databind.ObjectMapper;

/**
 * Tests for the UserController class, which handles user-related HTTP requests.
 */
@WebMvcTestSecured(controllers = UserController.class)
public class UserControllerTest {
    /** The MockMvc instance for testing the controller. */
    @Autowired
    private MockMvc mockMvc;

    /** The UserService instance for testing. */
    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    /** The ObjectMapper instance for testing. */
    @Autowired
    private ObjectMapper objectMapper;

    /** The utility class for creating test user data. */
    private UserTestUtils userTestUtils;

    /** 
     * Constructor for the UserControllerTest class, which initializes the UserTestUtils instance.
     */
    public UserControllerTest() {
        userTestUtils = new UserTestUtils();
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
}
