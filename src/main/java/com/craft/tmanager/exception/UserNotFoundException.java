package com.craft.tmanager.exception;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String username) {
        super("User not found with username: " + username);
    }
}
