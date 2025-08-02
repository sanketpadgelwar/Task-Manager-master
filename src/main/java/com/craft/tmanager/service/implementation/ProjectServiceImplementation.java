package com.craft.tmanager.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.craft.tmanager.dto.ProjectDTO;
import com.craft.tmanager.entity.Project;
import com.craft.tmanager.exception.ProjectNotFoundException;
import com.craft.tmanager.repository.ProjectRepository;
import com.craft.tmanager.repository.UserRepository;
import com.craft.tmanager.service.definition.ProjectServiceDefinition;

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
            .collect(Collectors.toList());
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        // Convert ProjectDTO to Project entity
    	
        Project project = convertToEntity(projectDTO);
        // Add logic to validate project data and perform creation
        project.setLastUpdatedOn(LocalDateTime.now());
        Project createdProject = projectRepository.save(project);
        
        // Convert created Project entity back to ProjectDTO
        return Optional.of(projectRepository.save(project))
                        .map(this::convertToDTO)
                        .orElseThrow(() -> new RuntimeException("Project creation failed"));
    }

    public ProjectDTO getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                                .map(this::convertToDTO)
                                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }
    
    @Override
	public List<ProjectDTO> getProjectsByManagerId(Long managerId) {
    	return projectRepository.findByManagerId(userRepository.getById(managerId))
    	    .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
	}

    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        // Convert ProjectDTO to Project entity
        Project project = convertToEntity(projectDTO);
        project.setProjectId(projectId);
        
        // Add logic to update project information
        Project updatedProject = projectRepository.save(project);
        
        // Convert updated Project entity back to ProjectDTO
        return Optional.of(projectRepository.save(project))
                        .map(this::convertToDTO)
                        .orElseThrow(() -> new RuntimeException("Project update failed"));
    }
    
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
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
        project.setManagerId(userRepository.getById(projectDTO.getManagerId()));
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

	
}
