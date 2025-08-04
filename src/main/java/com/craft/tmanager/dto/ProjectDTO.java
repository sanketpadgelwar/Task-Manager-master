package com.craft.tmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectDTO {

    private Long projectId; // Usually auto-generated, no validation needed

    @NotBlank(message = "Project name cannot be empty")
    @Size(max = 100, message = "Project name cannot exceed 100 characters")
    private String projectName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    @FutureOrPresent(message = "End date cannot be in the past")
    private LocalDate endDate;

    @PastOrPresent(message = "Last updated date cannot be in the future")
    private LocalDateTime lastUpdatedOn;

    @NotNull(message = "Manager ID is required")
    private Long managerId;
}