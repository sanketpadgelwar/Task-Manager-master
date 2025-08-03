package com.craft.tmanager.service.definition;

import java.util.List;
import org.springframework.stereotype.Service;
import com.craft.tmanager.dto.UserDTO;
import com.craft.tmanager.entity.UserRole;


@Service
public interface UserServiceDefinition {
	public List<UserDTO> getAllUsers();
	public UserDTO registerUser(UserDTO userDTO);
	public UserDTO getUserByUsername(String username);
	public List<UserDTO> getUsersByRole(UserRole role);
	public UserDTO updateUser(Long userId, UserDTO updatedUserDTO);
	public void deleteUser(Long userId);
	public UserDTO getUserById(Long id);
	
	 

}
