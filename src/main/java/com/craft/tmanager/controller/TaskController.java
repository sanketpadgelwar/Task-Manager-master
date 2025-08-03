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
import com.craft.tmanager.dto.TaskDTO;
import com.craft.tmanager.dto.UserDTO;
import com.craft.tmanager.exception.InvalidTaskDeadlineException;
import com.craft.tmanager.service.definition.TaskServiceDefinition;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
   
	@Autowired
    private TaskServiceDefinition taskService;


	@GetMapping
	public ResponseEntity<List<TaskDTO>> getAllTasks() {
	    List<TaskDTO> tasks = taskService.getAllTasks();
	    return ResponseEntity.ok(tasks);
	}
	 
    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) throws InvalidTaskDeadlineException {
//    	System.out.println(taskDTO);
    	TaskDTO createdTask = taskService.createTask(taskDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId) {
        TaskDTO taskDTO = taskService.getTaskById(taskId);
        return ResponseEntity.ok(taskDTO);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDTO) throws InvalidTaskDeadlineException {
        TaskDTO updatedTask = taskService.updateTask(taskId, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("tasks/projectId/{projectId}")
    public ResponseEntity<List<TaskDTO>> getTasksByProjectId(@PathVariable Long projectId) {
        List<TaskDTO> tasks = taskService.getTasksByProject(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("users/projectId/{projectId}")
    public ResponseEntity<List<UserDTO>> getEmployeesByProjectId(@PathVariable Long projectId) {
        List<UserDTO> tasks = taskService.getEmployeesByProject(projectId);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/last-activities")
    public List<TaskDTO> getLastUpdatedTasks() {
        return taskService.getLastUpdatedTasks();
    }

    @GetMapping("/tasks/employeeId/{employeeId}")
    public ResponseEntity<List<TaskDTO>> getTasksByEmployeeId(@PathVariable Long employeeId) {
        List<TaskDTO> tasks = taskService.getTasksByEmployee(employeeId);
        return ResponseEntity.ok(tasks);
    }
    // Add more endpoints as needed
}


