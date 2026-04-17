package com.todoapp.ToDoApp.service;

import com.todoapp.ToDoApp.dto.TodoDTO;
import com.todoapp.ToDoApp.entity.Todo;
import com.todoapp.ToDoApp.exception.ResourceNotFoundException;
import com.todoapp.ToDoApp.mapper.TodoMapper;
import com.todoapp.ToDoApp.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // marks as service layer
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repository;

    // constructor injection (important rule)
    public TodoServiceImpl(TodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public TodoDTO createTodo(TodoDTO dto) {
        Todo todo = TodoMapper.toEntity(dto);
        return TodoMapper.toDTO(repository.save(todo));
    }

    @Override
    public List<TodoDTO> getAllTodos() {
        return repository.findAll()
                .stream()
                .map(TodoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TodoDTO getTodoById(Long id) {
        Todo todo = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));

        return TodoMapper.toDTO(todo);
    }

    @Override
    public TodoDTO updateTodo(Long id, TodoDTO dto) {
        Todo todo = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));

        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());

        return TodoMapper.toDTO(repository.save(todo));
    }

    @Override
    public void deleteTodo(Long id) {
        Todo todo = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));

        repository.delete(todo);
    }
}