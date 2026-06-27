package com.todoapp.ToDoApp.repository;

import com.todoapp.ToDoApp.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository gives CRUD methods automatically
public interface TodoRepository extends JpaRepository<Todo, Long> {
}