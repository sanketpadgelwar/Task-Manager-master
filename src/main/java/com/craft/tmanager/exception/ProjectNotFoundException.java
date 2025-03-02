package com.craft.tmanager.exception;

public class ProjectNotFoundException extends CustomException {
    public ProjectNotFoundException(Long projectId) {
        super("Project not found with ID: " + projectId);
    }
}
