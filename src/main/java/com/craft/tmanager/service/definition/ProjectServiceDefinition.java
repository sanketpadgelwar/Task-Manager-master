package com.craft.tmanager.service.definition;

import java.util.List;

import org.springframework.stereotype.Service;

import com.craft.tmanager.dto.ProjectDTO;
import com.craft.tmanager.entity.Project;

@Service
public interface ProjectServiceDefinition {
	
	public List<ProjectDTO> getAllProjects();
	public ProjectDTO createProject(ProjectDTO projectDTO);
	public ProjectDTO getProjectById(Long projectId);
	public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO);
	public void deleteProject(Long projectId);
	public List<ProjectDTO> getProjectsByManagerId(Long managerId);
	
	
}
