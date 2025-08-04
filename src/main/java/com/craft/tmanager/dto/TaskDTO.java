package com.craft.tmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.craft.tmanager.entity.TaskPriority;
import com.craft.tmanager.entity.TaskStatus;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;


@Data
public class TaskDTO {

    private Long taskId; // Usually auto-generated, no validation needed

    @NotBlank(message = "Task name cannot be empty")
    @Size(max = 100, message = "Task name cannot exceed 100 characters")
    private String taskName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Task status is required")
    private TaskStatus status;

    @NotNull(message = "Task priority is required")
    private TaskPriority priority;

    @FutureOrPresent(message = "Deadline cannot be in the past")
    private LocalDate deadline;

    @PastOrPresent(message = "Last updated date cannot be in the future")
    private LocalDateTime lastUpdatedOn;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Assigned User ID is required")
    private Long assignedUserId;
}
