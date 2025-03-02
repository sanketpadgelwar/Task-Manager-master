package com.craft.tmanager.exception;

public class TaskNotFoundException extends CustomException {
    public TaskNotFoundException(Long taskId) {
        super("Task not found with ID: " + taskId);
    }
}

