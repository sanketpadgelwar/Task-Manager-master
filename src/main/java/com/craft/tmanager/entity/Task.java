package com.craft.tmanager.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    
    @Column(nullable = false)
    private String taskName;
    
    @Column
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;
    
    @Column(nullable = false)
    private LocalDate deadline;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project projectId;
    
    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUserId;
    
    
    @Column(name = "last_updated_on", nullable = false)
    private LocalDateTime lastUpdatedOn;
    
    @PreUpdate
    public void preUpdate() {
        lastUpdatedOn = LocalDateTime.now();
    }
    // Getters and setters
}

