package com.craft.tmanager.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.craft.tmanager.dto.ProjectDTO;
import com.craft.tmanager.entity.Project;
import com.craft.tmanager.exception.ProjectNotFoundException;
import com.craft.tmanager.repository.ProjectRepository;
import com.craft.tmanager.repository.UserRepository;
import com.craft.tmanager.service.definition.ProjectServiceDefinition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectServiceImplementation implements ProjectServiceDefinition{
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;
    
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .peek(project -> log.info("Project '{}' fetched by '{}'", 
                                      project.getProjectName(), getCurrentUser()))
            .collect(Collectors.toList());
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = convertToEntity(projectDTO);
        project.setLastUpdatedOn(LocalDateTime.now());

        return Optional.of(projectRepository.save(project))
                .map(createdProject -> {
                    log.info("Project '{}' created successfully by '{}'",
                             createdProject.getProjectName(), getCurrentUser());
                    return convertToDTO(createdProject);
                })
                .orElseThrow(() -> {
                    log.error("Project creation failed for '{}', requested by '{}'",
                              projectDTO.getProjectName(), getCurrentUser());
                    return new RuntimeException("Project creation failed");
                });
    }

    public ProjectDTO getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .map(project -> {
                    log.info("Project with ID '{}' fetched by '{}'",
                             projectId, getCurrentUser());
                    return convertToDTO(project);
                })
                .orElseThrow(() -> {
                    log.error("Project with ID '{}' not found. Requested by '{}'",
                              projectId, getCurrentUser());
                    return new ProjectNotFoundException(projectId);
                });
    }
    
    @Override
	public List<ProjectDTO> getProjectsByManagerId(Long managerId) {
    	return projectRepository.findByManagerId(userRepository.findById(managerId).get())
            .stream()
            .map(this::convertToDTO)
            .peek(project -> log.info("Project '{}' for manager '{}' fetched by '{}'",
                                      project.getProjectName(), managerId, getCurrentUser()))
            .collect(Collectors.toList());
    }

    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        Project project = convertToEntity(projectDTO);
        project.setProjectId(projectId);
        project.setLastUpdatedOn(LocalDateTime.now());

        return Optional.of(projectRepository.save(project))
                .map(updatedProject -> {
                    log.info("Project with ID '{}' updated successfully by '{}'",
                             projectId, getCurrentUser());
                    return convertToDTO(updatedProject);
                })
                .orElseThrow(() -> {
                    log.error("Project update failed for ID '{}' by '{}'",
                              projectId, getCurrentUser());
                    return new RuntimeException("Project update failed");
                });
    }
    
    public void deleteProject(Long projectId) {
        projectRepository.findById(projectId)
            .ifPresentOrElse(project -> {
                projectRepository.deleteById(projectId);
                log.warn("Project '{}' with ID '{}' deleted by '{}'",
                         project.getProjectName(), projectId, getCurrentUser());
            }, () -> {
                log.error("Delete failed: Project with ID '{}' not found. Requested by '{}'",
                          projectId, getCurrentUser());
                throw new ProjectNotFoundException(projectId);
            });
    }

    // Add more methods as needed
    
    // Helper method to convert ProjectDTO to Project entity
    private Project convertToEntity(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setProjectName(projectDTO.getProjectName());
        project.setDescription(projectDTO.getDescription());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setLastUpdatedOn(projectDTO.getLastUpdatedOn());
        project.setManagerId(userRepository.findById(projectDTO.getManagerId()).get());
        System.out.println(project);
        return project;
    }

    // Helper method to convert Project entity to ProjectDTO
    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setEndDate(project.getEndDate());
        projectDTO.setLastUpdatedOn(project.getLastUpdatedOn());
     
        projectDTO.setManagerId(project.getManagerId().getUserId());
        
        return projectDTO;
    }

	private String getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
        return "SYSTEM"; // Default if no user is logged in
    }

    return authentication.getName(); // Returns the username of the logged-in user
    }
}
