package com.craft.tmanager.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.craft.tmanager.dto.TaskDTO;
import com.craft.tmanager.dto.UserDTO;
import com.craft.tmanager.entity.Task;
import com.craft.tmanager.exception.InvalidTaskDeadlineException;
import com.craft.tmanager.exception.TaskNotFoundException;
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
		List<Task> tasks = taskRepository.findByProjectId(projectRepository.findById(projectId));
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        System.out.println(tasks);
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    public TaskDTO createTask(TaskDTO taskDTO) throws InvalidTaskDeadlineException {
//    	System.out.println(taskDTO);
    	 LocalDate currentDateTime = LocalDate.now();
         if (!taskDTO.getDeadline().isAfter(currentDateTime)) {
             throw new InvalidTaskDeadlineException("Deadline must be a future date");
         }
        // Convert TaskDTO to Task entity
         
        Task task = convertToEntity(taskDTO);
        task.setLastUpdatedOn(LocalDateTime.now());
        
        // Add logic to validate task data and perform creation
        Task createdTask = taskRepository.save(task);
        
        // Convert created Task entity back to TaskDTO
        return convertToDTO(createdTask);
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
        
        // Add logic to update task information
        Task updatedTask = taskRepository.save(task);
        
        // Convert updated Task entity back to TaskDTO
        return convertToDTO(updatedTask);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
    
    public List<TaskDTO> getLastUpdatedTasks() {
        
        List<Task> tasks = taskRepository.findAllOrderByLastUpdatedOnDesc();
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
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
        task.setProjectId(projectRepository.getById(taskDTO.getProjectId()));
        task.setAssignedUserId(userRepository.getById(taskDTO.getAssignedUserId()));
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

	@Override
	public List<UserDTO> getEmployeesByProject(Long projectId) {
		List<Task> tasks = taskRepository.findByProjectId(projectRepository.findById(projectId));
		UserServiceImplementation us = new UserServiceImplementation();
		System.out.println(tasks);
		List<UserDTO> users = new ArrayList<>();
		for(Task task : tasks) {
			UserDTO user = us.convertToDTO(task.getAssignedUserId());
			if(users.contains(user)) {
				continue;
			}else {
				users.add(user);
			}
			//userRepository.findById(task.getAssignedUserId().)
		}
		System.out.println(users);
		return users;
	}

    // Add more methods as needed
    
    
}
