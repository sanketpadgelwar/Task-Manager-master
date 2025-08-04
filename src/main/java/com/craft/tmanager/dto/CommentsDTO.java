package com.craft.tmanager.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CommentsDTO {

    private Long commentId; 
    @NotBlank(message = "Comment content cannot be empty")
    @Size(max = 500, message = "Comment content cannot exceed 500 characters")
    private String content;

    @NotNull(message = "Task ID is required")
    private Long taskId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt;

}