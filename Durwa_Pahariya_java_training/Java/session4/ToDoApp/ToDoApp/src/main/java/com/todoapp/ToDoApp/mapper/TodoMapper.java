package com.todoapp.ToDoApp.mapper;

import com.todoapp.ToDoApp.dto.TodoDTO;
import com.todoapp.ToDoApp.entity.Todo;
import com.todoapp.ToDoApp.entity.TodoStatus;

import java.time.LocalDateTime;

public class TodoMapper {

    // DTO → Entity
    public static Todo toEntity(TodoDTO dto) {
        Todo todo = new Todo();

        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());

        // if status not given then the default is PENDING
        if (dto.getStatus() != null) {
            todo.setStatus(TodoStatus.valueOf(dto.getStatus()));
        } else {
            todo.setStatus(TodoStatus.PENDING);
        }

        todo.setCreatedAt(LocalDateTime.now()); // set current time
        return todo;
    }

    // Entity → DTO
    public static TodoDTO toDTO(Todo todo) {
        TodoDTO dto = new TodoDTO();
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setStatus(todo.getStatus().name());



        return dto;
    }
}