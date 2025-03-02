package com.craft.tmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProjectDTO {
	private Long projectId;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime lastUpdatedOn;
    private Long managerId;

    // Getters and setters
}

