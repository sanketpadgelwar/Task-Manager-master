package com.craft.tmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craft.tmanager.entity.Project;
import com.craft.tmanager.entity.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByManagerId(User manager);
    Optional<Project> findByProjectId(Long projectId);
}

