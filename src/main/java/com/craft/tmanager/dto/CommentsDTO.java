package com.craft.tmanager.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentsDTO {
    private Long commentId;
    private String content;
    private Long taskId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
