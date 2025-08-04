package com.craft.tmanager.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificationDTO {

    private Long notificationId; // Usually auto-generated

    @NotBlank(message = "Notification message cannot be empty")
    @Size(max = 255, message = "Notification message cannot exceed 255 characters")
    private String message;

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;

    private boolean readStatus; // Default is false, no validation needed

    @NotNull(message = "User ID is required")
    private Long userId;
}
