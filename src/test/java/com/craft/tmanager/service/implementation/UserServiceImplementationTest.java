package com.craft.tmanager.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.craft.tmanager.dto.UserDTO;
import com.craft.tmanager.entity.User;
import com.craft.tmanager.entity.UserRole;
import com.craft.tmanager.exception.UserNotFoundException;
import com.craft.tmanager.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImplementation userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setRole(UserRole.PROJECT_MANAGER);

        userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setUsername("johndoe");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setRole(UserRole.TASK_ASSIGNEE);
    }

    @Test
    public void getAllUsers_ShouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<UserDTO> allUsers = userService.getAllUsers();

        assertThat(allUsers).hasSize(1);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void registerUser_ShouldSaveAndReturnUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO registeredUser = userService.registerUser(userDTO);

        assertThat(registeredUser.getUsername()).isEqualTo("johndoe");
        verify(userRepository, times(1)).save(any(User.class));
    }

//    @Test
//    public void getUserByUsername_ShouldReturnUser() {
//        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
//
//        UserDTO foundUser = userService.getUserByUsername("johndoe");
//
//        assertThat(foundUser.getUsername()).isEqualTo("johndoe");
//        verify(userRepository, times(1)).findByUsername(any(String.class));
//    }

    @Test
    public void getUserByUsername_ShouldThrowUserNotFoundException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(null);

        assertThatThrownBy(() -> userService.getUserByUsername("unknown"))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("unknown");
    }

    @Test
    public void getUserById_ShouldReturnUser() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        UserDTO foundUser = userService.getUserById(1L);

        assertThat(foundUser.getUsername()).isEqualTo("johndoe");
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void getUserById_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(1L))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("1");
    }

    @Test
    public void getUsersByRole_ShouldReturnUsersByRole() {
        when(userRepository.findByRole(any(UserRole.class))).thenReturn(Arrays.asList(user));

        List<UserDTO> usersByRole = userService.getUsersByRole(UserRole.TASK_ASSIGNEE);

        assertThat(usersByRole).hasSize(1);
        verify(userRepository, times(1)).findByRole(any(UserRole.class));
    }

    @Test
    public void getUsersByRole_ShouldThrowUserNotFoundException() {
        when(userRepository.findByRole(any(UserRole.class))).thenReturn(Arrays.asList());

        assertThatThrownBy(() -> userService.getUsersByRole(UserRole.TASK_ASSIGNEE))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("No users found with role: USER");
    }

    @Test
    public void updateUser_ShouldUpdateAndReturnUser() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setUsername("updatedUser");
        updatedUserDTO.setEmail("updated@example.com");
        updatedUserDTO.setRole(UserRole.ADMIN);

        UserDTO updatedUser = userService.updateUser(1L, updatedUserDTO);

        assertThat(updatedUser.getUsername()).isEqualTo("updatedUser");
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateUser_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setUsername("updatedUser");
        updatedUserDTO.setEmail("updated@example.com");
        updatedUserDTO.setRole(UserRole.ADMIN);

        assertThatThrownBy(() -> userService.updateUser(1L, updatedUserDTO))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("1");
    }

    @Test
    public void deleteUser_ShouldDeleteUser() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(any(Long.class));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).findById(any(Long.class));
        verify(userRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    public void deleteUser_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(1L))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("1");
    }
}
