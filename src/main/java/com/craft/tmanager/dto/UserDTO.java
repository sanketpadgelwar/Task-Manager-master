package com.craft.tmanager.dto;

import com.craft.tmanager.entity.UserRole;

import lombok.Data;

@Data
public class UserDTO {
	private Long userId;
    private String username;
    private String email;
    private String password;
    private UserRole role;

    // Getters and setters
}

