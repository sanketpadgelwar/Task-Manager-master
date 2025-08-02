package com.craft.tmanager.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NotificationDTO {
    
    private Long notificationId;
    private String message;
    private LocalDateTime timestamp;
    private boolean readStatus;
    private Long userId;

}
