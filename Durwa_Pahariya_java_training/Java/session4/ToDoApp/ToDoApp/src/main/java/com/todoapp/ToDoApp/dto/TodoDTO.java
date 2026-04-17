package com.todoapp.ToDoApp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TodoDTO {

    @NotNull // cannot be null
    @Size(min = 3) // minimum 3 characters
    private String title;

    private String description;

    private String status; // using String for gaining flexibility

    // getters & setters

    public String getTitle() {
        return title;
    }
// this refers to current object
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}