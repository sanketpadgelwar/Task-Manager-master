package com.craft.tmanager.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.craft.tmanager.dto.ProjectDTO;
import com.craft.tmanager.entity.Project;
import com.craft.tmanager.entity.User;
import com.craft.tmanager.exception.ProjectNotFoundException;
import com.craft.tmanager.repository.ProjectRepository;
import com.craft.tmanager.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplementationTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectServiceImplementation projectService;

    private Project project;
    private ProjectDTO projectDTO;
    private User manager;

    @BeforeEach
    public void setUp() {
        manager = new User();
        manager.setUserId(1L);

        project = new Project();
        project.setProjectId(1L);
        project.setProjectName("Test Project");
        project.setDescription("Test Description");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(10));
        project.setLastUpdatedOn(LocalDateTime.now());
        project.setManagerId(manager);

        projectDTO = new ProjectDTO();
        projectDTO.setProjectId(1L);
        projectDTO.setProjectName("Test Project");
        projectDTO.setDescription("Test Description");
        projectDTO.setStartDate(LocalDate.now());
        projectDTO.setEndDate(LocalDate.now().plusDays(10));
        projectDTO.setLastUpdatedOn(LocalDateTime.now());
        projectDTO.setManagerId(1L);
    }

    @Test
    public void getAllProjects_ShouldReturnAllProjects() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project));

        List<ProjectDTO> projectDTOs = projectService.getAllProjects();

        assertThat(projectDTOs).hasSize(1);
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    public void createProject_ShouldCreateAndReturnProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(userRepository.findById(any(Long.class)).get()).thenReturn(manager);

        ProjectDTO createdProject = projectService.createProject(projectDTO);

        assertThat(createdProject.getProjectName()).isEqualTo(projectDTO.getProjectName());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    public void getProjectById_ShouldReturnProject_WhenProjectExists() {
        when(projectRepository.findById(any(Long.class))).thenReturn(Optional.of(project));

        ProjectDTO foundProject = projectService.getProjectById(1L);

        assertThat(foundProject.getProjectId()).isEqualTo(project.getProjectId());
        verify(projectRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void getProjectById_ShouldThrowException_WhenProjectDoesNotExist() {
        when(projectRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.getProjectById(1L))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(projectRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void getProjectsByManagerId_ShouldReturnProjectsByManagerId() {
        when(userRepository.findById(any(Long.class)).get()).thenReturn(manager);
        when(projectRepository.findByManagerId(any(User.class))).thenReturn(Arrays.asList(project));

        List<ProjectDTO> projectsByManager = projectService.getProjectsByManagerId(1L);

        assertThat(projectsByManager).hasSize(1);
        verify(projectRepository, times(1)).findByManagerId(any(User.class));
    }

    @Test
    public void updateProject_ShouldUpdateAndReturnProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(userRepository.findById(any(Long.class)).get()).thenReturn(manager);

        ProjectDTO updatedProject = projectService.updateProject(1L, projectDTO);

        assertThat(updatedProject.getProjectId()).isEqualTo(projectDTO.getProjectId());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    public void deleteProject_ShouldDeleteProject() {
        doNothing().when(projectRepository).deleteById(any(Long.class));

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).deleteById(any(Long.class));
    }
}
