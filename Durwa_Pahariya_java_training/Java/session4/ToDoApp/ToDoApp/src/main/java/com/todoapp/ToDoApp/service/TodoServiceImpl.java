package com.todoapp.ToDoApp.service;

import com.todoapp.ToDoApp.dto.TodoDTO;
import com.todoapp.ToDoApp.entity.Todo;
import com.todoapp.ToDoApp.exception.ResourceNotFoundException;
import com.todoapp.ToDoApp.mapper.TodoMapper;
import com.todoapp.ToDoApp.repository.TodoRepository;
import com.todoapp.ToDoApp.client.NotificationServiceClient;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service // service layer
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repository;
    private final NotificationServiceClient notificationClient;

    // logger for service layer
    private static final Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);

    // constructor injection i.e. dependencies injection
    public TodoServiceImpl(TodoRepository repository,
                           NotificationServiceClient notificationClient) {
        this.repository = repository;
        this.notificationClient = notificationClient;
    }

    @Override
    public TodoDTO createTodo(TodoDTO dto) {

        logger.info("Creating new TODO");

        // convert DTO to entity
        Todo todo = TodoMapper.toEntity(dto);

        // save to database
        Todo saved = repository.save(todo);

        logger.info("TODO created with id: {}", saved.getId());

        //  external service call
        notificationClient.sendNotification("Notification sent for new TODO");

        return TodoMapper.toDTO(saved);
    }

    @Override
    public List<TodoDTO> getAllTodos() {

        logger.info("Fetching all TODOs from database");

        return repository.findAll()
                .stream()
                .map(TodoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TodoDTO getTodoById(Long id) {

        logger.info("Fetching TODO with id: {}", id);

        Todo todo = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("TODO not found with id: {}", id);
                    return new ResourceNotFoundException("Todo not found");
                });

        return TodoMapper.toDTO(todo);
    }

    @Override
    public TodoDTO updateTodo(Long id, TodoDTO dto) {

        logger.info("Updating TODO with id: {}", id);

        Todo todo = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("TODO not found with id: {}", id);
                    return new ResourceNotFoundException("Todo not found");
                });

        // update fields
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());

        Todo updated = repository.save(todo);

        logger.info("TODO updated successfully with id: {}", id);

        return TodoMapper.toDTO(updated);
    }

    @Override
    public void deleteTodo(Long id) {

        logger.info("Deleting TODO with id: {}", id);

        Todo todo = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("TODO not found with id: {}", id);
                    return new ResourceNotFoundException("Todo not found");
                });

        repository.delete(todo);

        logger.info("TODO deleted successfully with id: {}", id);
    }
}