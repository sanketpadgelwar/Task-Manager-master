package com.craft.tmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craft.tmanager.entity.User;
import com.craft.tmanager.entity.UserRole;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(UserRole role);
	//List<User> findByRoles(UserRole role);
}

