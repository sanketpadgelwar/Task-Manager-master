package com.craft.tmanager.exception;

public class InvalidDataException extends CustomException {
    public InvalidDataException(String message) {
        super("Invalid data: " + message);
    }
}
