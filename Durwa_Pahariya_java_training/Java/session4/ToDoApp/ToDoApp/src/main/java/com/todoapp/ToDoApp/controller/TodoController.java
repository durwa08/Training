package com.todoapp.ToDoApp.controller;

import com.todoapp.ToDoApp.dto.TodoDTO;
import com.todoapp.ToDoApp.service.TodoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService service;

    // logger for tracking API calls
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    public TodoController(TodoService service) {
        this.service = service;
    }

    @PostMapping
    public TodoDTO create(@Valid @RequestBody TodoDTO dto) {
        logger.info("Received request to create TODO");
        return service.createTodo(dto);
    }

    @GetMapping
    public List<TodoDTO> getAll() {
        logger.info("Fetching all TODOs");
        return service.getAllTodos();
    }

    @GetMapping("/{id}")
    public TodoDTO getById(@PathVariable Long id) {
        logger.info("Fetching TODO with id: {}", id);
        return service.getTodoById(id);
    }

    @PutMapping("/{id}")
    public TodoDTO update(@PathVariable Long id, @Valid @RequestBody TodoDTO dto) {
        logger.info("Updating TODO with id: {}", id);
        return service.updateTodo(id, dto);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        logger.info("Deleting TODO with id: {}", id);
        service.deleteTodo(id);
        return "Deleted successfully";
    }
}