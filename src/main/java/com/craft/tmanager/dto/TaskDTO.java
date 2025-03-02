package com.craft.tmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.craft.tmanager.entity.TaskPriority;
import com.craft.tmanager.entity.TaskStatus;

import lombok.Data;

@Data
public class TaskDTO {
	
	private Long taskId;
    private String taskName;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate deadline;
    private LocalDateTime lastUpdatedOn;
    private Long projectId;
    private Long assignedUserId;

    // Getters and setters
}

