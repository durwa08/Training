package com.todoapp.ToDoApp.service;

import com.todoapp.ToDoApp.client.NotificationServiceClient;
import com.todoapp.ToDoApp.dto.TodoDTO;
import com.todoapp.ToDoApp.entity.Todo;
import com.todoapp.ToDoApp.entity.TodoStatus;
import com.todoapp.ToDoApp.repository.TodoRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceImplTest {

    // mock repository
    private final TodoRepository repository = Mockito.mock(TodoRepository.class);

    // mock external notification service
    private final NotificationServiceClient client = Mockito.mock(NotificationServiceClient.class);

    // injecting mocks into service
    private final TodoServiceImpl service =
            new TodoServiceImpl(repository, client);

    @Test
    void testCreateTodo() {

        // input DTO
        TodoDTO dto = new TodoDTO();
        dto.setTitle("Test Task");

        // mocked saved entity (what DB returns)
        Todo saved = new Todo();
        saved.setId(1L);
        saved.setTitle("Test Task");
        saved.setStatus(TodoStatus.PENDING); // important to avoid null

        // mock save behavior
        Mockito.when(repository.save(Mockito.any(Todo.class)))
                .thenReturn(saved);

        // call service
        TodoDTO result = service.createTodo(dto);

        // verify result
        assertEquals("Test Task", result.getTitle());
    }

    @Test
    void testGetTodoById() {

        // dummy entity
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Task");
        todo.setStatus(TodoStatus.PENDING); // important fix

        // mock findById
        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(todo));

        // call service
        TodoDTO result = service.getTodoById(1L);

        // verify result
        assertEquals("Task", result.getTitle());
    }
}