package com.craft.tmanager.service.definition;

import java.util.List;

import org.springframework.stereotype.Service;

import com.craft.tmanager.dto.TaskDTO;
import com.craft.tmanager.dto.UserDTO;
import com.craft.tmanager.exception.InvalidTaskDeadlineException;

@Service
public interface TaskServiceDefinition {

	public List<TaskDTO> getTasksByProject(Long projectId);
	public List<TaskDTO> getAllTasks();
	public TaskDTO createTask(TaskDTO taskDTO) throws InvalidTaskDeadlineException;
	public TaskDTO getTaskById(Long taskId);
	public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) throws InvalidTaskDeadlineException;
	public void deleteTask(Long taskId);
	public List<UserDTO> getEmployeesByProject(Long projectId);
	public List<TaskDTO> getLastUpdatedTasks();
	
	
	
}
