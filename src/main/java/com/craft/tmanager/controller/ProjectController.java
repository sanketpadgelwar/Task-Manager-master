package com.craft.tmanager.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.craft.tmanager.dto.ProjectDTO;
import com.craft.tmanager.service.definition.ProjectServiceDefinition;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    private ProjectServiceDefinition projectService;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
    	
    	List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }
    @PostMapping("/create")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
    	System.out.println(projectDTO);
        ProjectDTO createdProject = projectService.createProject(projectDTO);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @GetMapping("/projectid/{projectId}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long projectId) {
    	ProjectDTO projectDTO = projectService.getProjectById(projectId);
        return ResponseEntity.ok(projectDTO);
    }
    
    @GetMapping("/managerid/{managerId}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByManagerId(@PathVariable Long managerId) {
    	List<ProjectDTO> projectDTO = projectService.getProjectsByManagerId(managerId);
        return ResponseEntity.ok(projectDTO);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long projectId, @RequestBody ProjectDTO projectDTO) {
        ProjectDTO updatedProject = projectService.updateProject(projectId, projectDTO);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
    
   
    // Add more endpoints as needed
}
