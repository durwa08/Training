package com.todoapp.ToDoApp.exception;

// custom exception when data not found
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}