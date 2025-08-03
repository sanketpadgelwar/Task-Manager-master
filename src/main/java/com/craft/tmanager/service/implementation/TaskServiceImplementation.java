package com.craft.tmanager.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
                .collect(Collectors.toList());
    }
    
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public TaskDTO createTask(TaskDTO taskDTO) throws InvalidTaskDeadlineException {

    	 LocalDate currentDateTime = LocalDate.now();
         if (!taskDTO.getDeadline().isAfter(currentDateTime)) {
             throw new InvalidTaskDeadlineException("Deadline must be a future date");
         }
        // Convert TaskDTO to Task entity
         
        Task task = convertToEntity(taskDTO);
        task.setLastUpdatedOn(LocalDateTime.now());
        
        return Optional.of(taskRepository.save(task))
                        .map(this::convertToDTO)
                        .orElseThrow(() -> new RuntimeException("Task creation failed"));
    }

    public TaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        return convertToDTO(task);
    }

    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) throws InvalidTaskDeadlineException {
    	LocalDate currentDateTime = LocalDate.now();
        if (!taskDTO.getDeadline().isAfter(currentDateTime)) {
            throw new InvalidTaskDeadlineException("Deadline must be a future date");
        }
        // Convert TaskDTO to Task entity
        Task task = convertToEntity(taskDTO);
        task.setTaskId(taskId);
        
        return Optional.of(taskRepository.save(task))
                        .map(this::convertToDTO)
                        .orElseThrow(() -> new RuntimeException("Task update failed"));
    }

    public void deleteTask(Long taskId) {
       taskRepository.findById(taskId)
                        .ifPresentOrElse(task -> taskRepository.deleteById(taskId), () -> {throw new TaskNotFoundException(taskId);}
                        );
    }
    
    public List<TaskDTO> getLastUpdatedTasks() {
        return taskRepository.findAllOrderByLastUpdatedOnDesc()
                             .stream()
                             .map(this::convertToDTO)
                             .collect(Collectors.toList());
    }
    
 
	@Override
	public List<UserDTO> getEmployeesByProject(Long projectId) {
        return taskRepository.findByProjectId(projectRepository.findById(projectId))
                .stream()
                .map(Task::getAssignedUserId)
                .distinct()
                .map(new UserServiceImplementation()::convertToDTO)
                .collect(Collectors.toList());
	}

    public List<TaskDTO> getTasksByEmployee(Long employeeId) {
        Optional<User> user = userRepository.findById(employeeId);
        if(user == null) throw new UserNotFoundException("Employee with id "+employeeId+" not found");
        return taskRepository.findByAssignedUserId(user.get())
        .stream()
        .map(this::convertToDTO)
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

    
    
    
}
