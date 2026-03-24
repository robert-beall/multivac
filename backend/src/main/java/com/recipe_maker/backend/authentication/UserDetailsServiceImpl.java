package com.recipe_maker.backend.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.recipe_maker.backend.users.User;
import com.recipe_maker.backend.users.UserRepository;

/**
 * Implementation of UserDetailsService for loading user details. 
 * 
 * This service is used by Spring Security to load user details during authentication.
 * It retrieves user information from the UserRepository and constructs a UserDetails object.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    /**
     * The UserRepository for accessing user data.
     */
    private final UserRepository userRepository;

    /**
     * Constructor for UserDetailsServiceImpl.
     * @param userRepository
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by username.
     * @param username The username of the user to load.
     * @return The UserDetails object for the specified user.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve the user from the repository based on the provided username
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Build and return a UserDetails object using the retrieved user information
        return user;
    }
}
