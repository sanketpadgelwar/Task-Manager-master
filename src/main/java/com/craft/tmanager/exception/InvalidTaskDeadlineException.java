package com.craft.tmanager.exception;

public class InvalidTaskDeadlineException extends Exception {
	
	public InvalidTaskDeadlineException(String message) {
        super("Invalid data: " + message);
    }

}
