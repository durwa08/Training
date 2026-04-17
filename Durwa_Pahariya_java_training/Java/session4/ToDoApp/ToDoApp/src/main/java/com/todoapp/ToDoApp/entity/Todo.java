package com.todoapp.ToDoApp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
// @Entity : tells Spring this class is a DB table
@Entity
@Table(name = "todos") // table name
public class Todo {

    // @ID tells that it is a primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING) // store enum as string in DB
    private TodoStatus status;

    // time when created
    private LocalDateTime createdAt;

    //----- getters & setters -----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}