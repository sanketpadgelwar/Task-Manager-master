package com.craft.tmanager.service.implementation;

import com.craft.tmanager.dto.TaskDTO;
import com.craft.tmanager.dto.UserDTO;
import com.craft.tmanager.entity.Project;
import com.craft.tmanager.entity.Task;
import com.craft.tmanager.entity.TaskPriority;
import com.craft.tmanager.entity.TaskStatus;
import com.craft.tmanager.entity.User;
import com.craft.tmanager.repository.ProjectRepository;
import com.craft.tmanager.repository.TaskRepository;
import com.craft.tmanager.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplementationTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImplementation taskService;

    private Task task;
    private TaskDTO taskDTO;
    private Project project;
    private User user;

    @BeforeEach
    public void setUp() {
        project = new Project();
        project.setProjectId(1L);

        user = new User();
        user.setUserId(1L);

        task = new Task();
        task.setTaskId(1L);
        task.setTaskName("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.ANALYSIS_WIP);
        task.setPriority(TaskPriority.HIGH);
        task.setDeadline(LocalDate.now().plusDays(1));
        task.setLastUpdatedOn(LocalDateTime.now());
        task.setProjectId(project);
        task.setAssignedUserId(user);

        taskDTO = new TaskDTO();
        taskDTO.setTaskId(1L);
        taskDTO.setTaskName("Test Task");
        taskDTO.setDescription("Test Description");
        taskDTO.setStatus(TaskStatus.ANALYSIS_WIP);
        taskDTO.setPriority(TaskPriority.HIGH);
        taskDTO.setDeadline(LocalDate.now().plusDays(1));
        taskDTO.setLastUpdatedOn(LocalDateTime.now());
        taskDTO.setProjectId(1L);
        taskDTO.setAssignedUserId(1L);
    }

    @Test
    public void getTasksByProject_ShouldReturnTasksByProject() {
        when(projectRepository.findById(any(Long.class))).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectId(any(Project.class))).thenReturn(Arrays.asList(task));

        List<TaskDTO> tasksByProject = taskService.getTasksByProject(1L);

        assertThat(tasksByProject).hasSize(1);
        verify(taskRepository, times(1)).findByProjectId(any(Project.class));
    }

    @Test
    public void getLastUpdatedTasks_ShouldReturnLastUpdatedTasks() {
        when(taskRepository.findAllOrderByLastUpdatedOnDesc()).thenReturn(Arrays.asList(task));

        List<TaskDTO> lastUpdatedTasks = taskService.getLastUpdatedTasks();

        assertThat(lastUpdatedTasks).hasSize(1);
        verify(taskRepository, times(1)).findAllOrderByLastUpdatedOnDesc();
    }

    @Test
    public void getEmployeesByProject_ShouldReturnEmployeesByProject() {
        when(projectRepository.findById(any(Long.class))).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectId(any(Project.class))).thenReturn(Arrays.asList(task));

        // Mock UserServiceImplementation
        UserServiceImplementation us = mock(UserServiceImplementation.class);
        when(us.convertToDTO(any(User.class))).thenReturn(new UserDTO());

        List<UserDTO> employeesByProject = taskService.getEmployeesByProject(1L);

        assertThat(employeesByProject).hasSize(1);
        verify(taskRepository, times(1)).findByProjectId(any(Project.class));
    }
}
