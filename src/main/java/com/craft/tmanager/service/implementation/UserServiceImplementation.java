package com.craft.tmanager.service.implementation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.craft.tmanager.dto.UserDTO;
import com.craft.tmanager.entity.User;
import com.craft.tmanager.entity.UserRole;
import com.craft.tmanager.exception.UserNotFoundException;
import com.craft.tmanager.repository.UserRepository;
import com.craft.tmanager.service.definition.UserServiceDefinition;


@Service
public class UserServiceImplementation implements UserServiceDefinition{
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();;
    
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public UserDTO registerUser(UserDTO userDTO) {
        // Convert UserDTO to User entity
        User user = convertToEntity(userDTO);
        
        // Add logic to validate user data and perform registration
        user.setPassword(encodePassword(userDTO.getPassword()));
        User registeredUser = userRepository.save(user);
        
        // Convert registered User entity back to UserDTO
        return convertToDTO(registeredUser);
    }

    public UserDTO getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        return convertToDTO(user.get());
    }
    
    @Override
	public UserDTO getUserById(Long id) {
    	Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(id.toString());
        }
		return this.convertToDTO(optionalUser.get());
	}

    public List<UserDTO> getUsersByRole(UserRole role) {
        List<User> users = userRepository.findByRole(role);
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found with role: " + role);
        }
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Add more methods as needed
    
   

	public UserDTO updateUser(Long userId, UserDTO updatedUserDTO) {
	        Optional<User> optionalUser = userRepository.findById(userId);
	        if (!optionalUser.isPresent()) {
	            throw new UserNotFoundException(userId.toString());
	        }
	        User user = optionalUser.get();
	        user.setUsername(updatedUserDTO.getUsername());
	        user.setEmail(updatedUserDTO.getEmail());
	        user.setRole(updatedUserDTO.getRole());
	        user.setPassword(encodePassword(updatedUserDTO.getPassword()));
	        // You can update other fields as needed
	        userRepository.save(user);
	        return convertToDTO(user);
	    }

	public void deleteUser(Long userId) {
	        // Add logic to delete a user by ID
	        Optional<User> optionalUser = userRepository.findById(userId);
	        if (!optionalUser.isPresent()) {
	            throw new UserNotFoundException(userId.toString());
	        }
	        userRepository.deleteById(userId);
	    }
	
	// Helper method to convert UserDTO to User entity
    private User convertToEntity(UserDTO userDTO) {
       User user = new User();
       user.setUsername(userDTO.getUsername());
       user.setEmail(userDTO.getEmail());
       user.setPassword(userDTO.getPassword());
       user.setRole(userDTO.getRole());
       return user;
   }

   // Helper method to convert User entity to UserDTO
    public UserDTO convertToDTO(User user) {
       UserDTO userDTO = new UserDTO();
       userDTO.setUserId(user.getUserId());
       userDTO.setUsername(user.getUsername());
       userDTO.setPassword(user.getPassword());  
       userDTO.setEmail(user.getEmail());
       userDTO.setRole(user.getRole());
       return userDTO;
   }

	
}

//@Service
//public class UserService {
//    @Autowired
//    private UserRepository userRepository;
//
//    public User registerUser(User user) {
//        // Add logic to validate user data and perform registration
//        return userRepository.save(user);
//    }
//
//    public User getUserByUsername(String username) {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new UserNotFoundException(username);
//        }
//        return user;
//    }
//
//    public List<User> getUsersByRole(UserRole role) {
//        List<User> users = userRepository.findByRole(role);
//        if (users.isEmpty()) {
//            throw new UserNotFoundException("No users found with role: " + role);
//        }
//        return users;
//    }
//    
//    public User updateUser(User user) {
//        // Add logic to update user information
//        return userRepository.save(user);
//    }
//
//    public void deleteUser(Long userId) {
//        // Add logic to delete a user by ID
//        Optional<User> optionalUser = userRepository.findById(userId);
//        if (!optionalUser.isPresent()) {
//            throw new UserNotFoundException(userId.toString());
//        }
//        userRepository.deleteById(userId);
//    }
//
//    public void changePassword(Long userId, String newPassword) {
//        // Add logic to change the password of a user
//        Optional<User> optionalUser = userRepository.findById(userId);
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            user.setPasswordHash(newPassword);
//            userRepository.save(user);
//        } else {
//            throw new UserNotFoundException(userId.toString());
//        }
//    }
//    
//    public User updateUser(Long userId, User updatedUser) {
//        Optional<User> optionalUser = userRepository.findById(userId);
//        if (!optionalUser.isPresent()) {
//            throw new UserNotFoundException(userId.toString());
//        }
//        User user = optionalUser.get();
//        user.setUsername(updatedUser.getUsername());
//        user.setEmail(updatedUser.getEmail());
//        user.setRole(updatedUser.getRole());
//        // You can update other fields as needed
//        return userRepository.save(user);
//    }
//
//    // Add more methods as needed
//}