package com.recipe_maker.backend.users;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {
    /** The repository for managing user data. */
    private UserRepository userRepository;

    /** The ModelMapper for converting between DTOs and entities. */
    private ModelMapper modelMapper;

    /** The PasswordEncoder for encoding user passwords. */
    private PasswordEncoder passwordEncoder;

    /**
     * Constructor for UserService.
     * @param userRepository
     * @param modelMapper
     * @param passwordEncoder
     */
    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    } 

    /**
     * Checks if a user with the given username exists.
     * @param username
     * @return boolean indicating whether a user with the given username exists
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if a user with the given email exists.
     * @param email
     * @return boolean indicating whether a user with the given email exists
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Registers a new user in the system.
     * @param userDTO
     */
    public void registerUser(UserDTO userDTO) {
        // Check for existing username and email
        if (existsByUsername(userDTO.getUsername())) {
            throw new DataIntegrityViolationException("Username already exists");
        }

        if (existsByEmail(userDTO.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        // Map DTO to entity, encode password, and save user
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
