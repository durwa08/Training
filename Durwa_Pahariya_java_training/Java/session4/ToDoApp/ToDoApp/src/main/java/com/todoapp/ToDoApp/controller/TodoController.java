package com.todoapp.ToDoApp.controller;

import com.todoapp.ToDoApp.dto.TodoDTO;
import com.todoapp.ToDoApp.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @PostMapping
    public TodoDTO create(@Valid @RequestBody TodoDTO dto) {
        return service.createTodo(dto);
    }

    @GetMapping
    public List<TodoDTO> getAll() {
        return service.getAllTodos();
    }

    @GetMapping("/{id}")
    public TodoDTO getById(@PathVariable Long id) {
        return service.getTodoById(id);
    }

    @PutMapping("/{id}")
    public TodoDTO update(@PathVariable Long id, @Valid @RequestBody TodoDTO dto) {
        return service.updateTodo(id, dto);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteTodo(id);
        return "Deleted successfully";
    }
}