package com.craft.tmanager.service.implementation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.craft.tmanager.dto.UserDTO;
import com.craft.tmanager.entity.User;
import com.craft.tmanager.entity.UserRole;
import com.craft.tmanager.exception.UserNotFoundException;
import com.craft.tmanager.repository.UserRepository;
import com.craft.tmanager.service.definition.UserServiceDefinition;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImplementation implements UserServiceDefinition {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.getAllUsers()
                .stream()
                .map(this::convertToDTO)
                .peek(user -> log.info("User '{}' fetched by '{}'", user.getUsername(), getCurrentUser()))
                .collect(Collectors.toList());
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
public UserDTO registerUser(UserDTO userDTO) {
    User user = convertToEntity(userDTO);
    user.setPassword(encodePassword(userDTO.getPassword()));

    return Optional.of(userRepository.save(user))
            .map(savedUser -> {
                // ✅ Log inside the map after successful save
                log.info("User '{}' registered successfully by '{}'",
                         savedUser.getUsername(), getCurrentUser());
                return convertToDTO(savedUser);
            })
            .orElseThrow(() -> new RuntimeException("User registration failed"));
}

    @Override
    public UserDTO getUserByUsername(String username) {
        return userRepository.findByUsername(username)
        .map(user -> {
            log.info("User '{}' fetched by '{}'", username, getCurrentUser());
            return convertToDTO(user);
        })
        .orElseThrow(() -> {
            log.error("User '{}' not found. Requested by '{}'", username, getCurrentUser());
            return new UserNotFoundException(username);
        });
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
        .map(user -> {
            log.info("User with ID '{}' fetched by '{}'", id, getCurrentUser());
            return convertToDTO(user);
        })
        .orElseThrow(() -> {
            log.error("User with ID '{}' not found. Requested by '{}'", id, getCurrentUser());
            return new UserNotFoundException(id.toString());
        });            
    }

    @Override
    public List<UserDTO> getUsersByRole(UserRole role) {
        return Optional.ofNullable(userRepository.findByRole(role))
        .orElseThrow(() -> {
            log.error("No users found with role '{}' requested by '{}'", role, getCurrentUser());
            return new UserNotFoundException("No users found with role: " + role);
        })
        .stream()
        .map(this::convertToDTO)
        .peek(user -> log.info("User '{}' with role '{}' fetched by '{}'",
                user.getUsername(), role, getCurrentUser()))
        .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO updatedUserDTO) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setUsername(updatedUserDTO.getUsername());
                    user.setEmail(updatedUserDTO.getEmail());
                    user.setRole(updatedUserDTO.getRole());
                    user.setPassword(encodePassword(updatedUserDTO.getPassword()));

                    User updatedUser = userRepository.save(user);
                    log.info("User with ID '{}' updated successfully by '{}'",
                            userId, getCurrentUser());

                    return convertToDTO(updatedUser);
                })
                .orElseThrow(() -> {
                    log.error("Update failed: User with ID '{}' not found. Requested by '{}'",
                            userId, getCurrentUser());
                    return new UserNotFoundException(userId.toString());
                });
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(user -> {
                    userRepository.deleteById(userId);
                    log.warn("User with ID '{}' deleted by '{}'", userId, getCurrentUser());
                }, () -> {
                    log.error("Delete failed: User with ID '{}' not found. Requested by '{}'",
                            userId, getCurrentUser());
                    throw new UserNotFoundException(userId.toString());
                });
    }

    // Helper: Convert DTO -> Entity
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        return user;
    }

    // Helper: Convert Entity -> DTO
    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());  // Optionally omit password for security
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private String getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
        return "SYSTEM"; // Default if no user is logged in
    }

    return authentication.getName(); // Returns the username of the logged-in user
    }
	
}