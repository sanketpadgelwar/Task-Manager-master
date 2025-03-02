package com.craft.tmanager.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "projects")
public class Project {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;
    
    @Column(nullable = false)
    private String projectName;
    
    @Column
    private String description;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate endDate;
    
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User managerId;
    
    @Column(nullable = false)
    private LocalDateTime lastUpdatedOn;
    // Getters and setters
}

