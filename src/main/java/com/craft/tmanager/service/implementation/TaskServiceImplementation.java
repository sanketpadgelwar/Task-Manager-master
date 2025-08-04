package com.craft.tmanager.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.craft.tmanager.dto.TaskDTO;
import com.craft.tmanager.dto.UserDTO;
import com.craft.tmanager.entity.Task;
import com.craft.tmanager.entity.User;
import com.craft.tmanager.exception.InvalidTaskDeadlineException;
import com.craft.tmanager.exception.TaskNotFoundException;
import com.craft.tmanager.exception.UserNotFoundException;
import com.craft.tmanager.repository.ProjectRepository;
import com.craft.tmanager.repository.TaskRepository;
import com.craft.tmanager.repository.UserRepository;
import com.craft.tmanager.service.definition.TaskServiceDefinition;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskServiceImplementation implements TaskServiceDefinition{
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private UserRepository userRepository;

	public List<TaskDTO> getTasksByProject(Long projectId){
        return taskRepository.findByProjectId(projectRepository.findById(projectId))
                .stream()
                .map(this::convertToDTO)
                .peek(task -> log.info("Task '{}' for project '{}' fetched by '{}'",
                        task.getTaskName(), projectId, getCurrentUser()))
                .collect(Collectors.toList());
    }
    
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .peek(task -> log.info("Task '{}' fetched by '{}'", task.getTaskName(), getCurrentUser()))
                .collect(Collectors.toList());
    }
    
    public TaskDTO createTask(TaskDTO taskDTO) throws InvalidTaskDeadlineException {

        LocalDate currentDate = LocalDate.now();
        if (!taskDTO.getDeadline().isAfter(currentDate)) {
            log.error("Task creation failed: Deadline '{}' is not in future. Requested by '{}'",
                    taskDTO.getDeadline(), getCurrentUser());
            throw new InvalidTaskDeadlineException("Deadline must be a future date");
        }

        Task task = convertToEntity(taskDTO);
        task.setLastUpdatedOn(LocalDateTime.now());

        return Optional.of(taskRepository.save(task))
                .map(savedTask -> {
                    log.info("Task '{}' created successfully by '{}'",
                            savedTask.getTaskName(), getCurrentUser());
                    return convertToDTO(savedTask);
                })
                .orElseThrow(() -> {
                    log.error("Task creation failed for '{}' by '{}'",
                            taskDTO.getTaskName(), getCurrentUser());
                    return new RuntimeException("Task creation failed");
                });
    }

    public TaskDTO getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    log.info("Task with ID '{}' fetched by '{}'", taskId, getCurrentUser());
                    return convertToDTO(task);
                })
                .orElseThrow(() -> {
                    log.error("Task with ID '{}' not found. Requested by '{}'", taskId, getCurrentUser());
                    return new TaskNotFoundException(taskId);
                });
    }

    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) throws InvalidTaskDeadlineException {
    	LocalDate currentDate = LocalDate.now();
        if (!taskDTO.getDeadline().isAfter(currentDate)) {
            log.error("Task update failed: Deadline '{}' is not in future. Requested by '{}'",
                    taskDTO.getDeadline(), getCurrentUser());
            throw new InvalidTaskDeadlineException("Deadline must be a future date");
        }

        Task task = convertToEntity(taskDTO);
        task.setTaskId(taskId);
        task.setLastUpdatedOn(LocalDateTime.now());

        return Optional.of(taskRepository.save(task))
                .map(updatedTask -> {
                    log.info("Task with ID '{}' updated successfully by '{}'", taskId, getCurrentUser());
                    return convertToDTO(updatedTask);
                })
                .orElseThrow(() -> {
                    log.error("Task update failed for ID '{}' by '{}'", taskId, getCurrentUser());
                    return new RuntimeException("Task update failed");
                });
    }

    public void deleteTask(Long taskId) {
       taskRepository.findById(taskId)
                .ifPresentOrElse(task -> {
                    taskRepository.deleteById(taskId);
                    log.warn("Task '{}' with ID '{}' deleted by '{}'",
                            task.getTaskName(), taskId, getCurrentUser());
                }, () -> {
                    log.error("Delete failed: Task with ID '{}' not found. Requested by '{}'",
                            taskId, getCurrentUser());
                    throw new TaskNotFoundException(taskId);
                });
    }
    
    public List<TaskDTO> getLastUpdatedTasks() {
        return taskRepository.findAllOrderByLastUpdatedOnDesc()
                .stream()
                .map(this::convertToDTO)
                .peek(task -> log.info("Task '{}' last updated on '{}' fetched by '{}'",
                        task.getTaskName(), task.getLastUpdatedOn(), getCurrentUser()))
                .collect(Collectors.toList());
    }
    
 
	@Override
	public List<UserDTO> getEmployeesByProject(Long projectId) {
        return taskRepository.findByProjectId(projectRepository.findById(projectId))
        .stream()
        .map(Task::getAssignedUserId)
        .distinct()
        .map(new UserServiceImplementation()::convertToDTO) // Consider refactoring to avoid `new` service call
        .peek(user -> log.info("Employee '{}' fetched for project '{}' by '{}'",
                user.getUsername(), projectId, getCurrentUser()))
        .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByEmployee(Long employeeId) {
        Optional<User> user = userRepository.findById(employeeId);
        if (user.isEmpty()) {
            log.error("Employee with ID '{}' not found. Requested by '{}'",
                    employeeId, getCurrentUser());
            throw new UserNotFoundException("Employee with id " + employeeId + " not found");
        }

        return taskRepository.findByAssignedUserId(user.get())
                .stream()
                .map(this::convertToDTO)
                .peek(task -> log.info("Task '{}' assigned to employee '{}' fetched by '{}'",
                        task.getTaskName(), employeeId, getCurrentUser()))
                .collect(Collectors.toList());
    }


// Helper method to convert TaskDTO to Task entity
    private Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTaskName(taskDTO.getTaskName());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        task.setDeadline(taskDTO.getDeadline());
        task.setLastUpdatedOn(taskDTO.getLastUpdatedOn());
        task.setProjectId(projectRepository.findByProjectId(taskDTO.getProjectId()).get());
        task.setAssignedUserId(userRepository.findById(taskDTO.getAssignedUserId()).get());
        return task;
    }

    // Helper method to convert Task entity to TaskDTO
    private TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(task.getTaskId());
        taskDTO.setTaskName(task.getTaskName());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setDeadline(task.getDeadline());
        taskDTO.setLastUpdatedOn(task.getLastUpdatedOn());
        taskDTO.setProjectId(task.getProjectId().getProjectId());
        taskDTO.setAssignedUserId(task.getAssignedUserId().getUserId());
        return taskDTO;
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "SYSTEM"; // Default for unauthenticated calls
        }
        return authentication.getName();
    }
    
    
}
