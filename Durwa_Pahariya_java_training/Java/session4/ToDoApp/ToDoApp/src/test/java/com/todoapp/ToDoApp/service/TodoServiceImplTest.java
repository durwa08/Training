package com.todoapp.ToDoApp.service;

import com.todoapp.ToDoApp.client.NotificationServiceClient;
import com.todoapp.ToDoApp.dto.TodoDTO;
import com.todoapp.ToDoApp.entity.Todo;
import com.todoapp.ToDoApp.entity.TodoStatus;
import com.todoapp.ToDoApp.repository.TodoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    private final TodoRepository repository = Mockito.mock(TodoRepository.class);
    private final NotificationServiceClient client = Mockito.mock(NotificationServiceClient.class);

    private final TodoServiceImpl service =
            new TodoServiceImpl(repository, client);


    // CREATE TODO
    @Test
    void testCreateTodo() {

        TodoDTO dto = new TodoDTO();
        dto.setTitle("Test Task");
        dto.setStatus(TodoStatus.PENDING.name());

        Todo saved = new Todo();
        saved.setId(1L);
        saved.setTitle("Test Task");
        saved.setStatus(TodoStatus.PENDING);

        Mockito.when(repository.save(Mockito.any(Todo.class)))
                .thenReturn(saved);

        TodoDTO result = service.createTodo(dto);

        assertEquals("Test Task", result.getTitle());

        Mockito.verify(repository).save(Mockito.any(Todo.class));
        Mockito.verify(client).sendNotification(Mockito.anyString());
    }


    // CREATE TODO

    @Test
    void testCreateTodo_EmptyTitle() {

        TodoDTO dto = new TodoDTO();
        dto.setTitle("");
        dto.setStatus(TodoStatus.PENDING.name());

        Todo saved = new Todo();
        saved.setId(1L);
        saved.setTitle("");
        saved.setStatus(TodoStatus.PENDING);

        Mockito.when(repository.save(Mockito.any(Todo.class)))
                .thenReturn(saved);

        TodoDTO result = service.createTodo(dto);

        assertEquals("", result.getTitle());
    }


    // GET TODO BY ID

    @Test
    void testGetTodoById() {

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Task");
        todo.setStatus(TodoStatus.PENDING);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(todo));

        TodoDTO result = service.getTodoById(1L);

        assertEquals("Task", result.getTitle());

        Mockito.verify(repository).findById(1L);
    }


    // GET TODO NOT FOUND

    @Test
    void testGetTodoById_NotFound() {

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () ->
                service.getTodoById(1L)
        );

        assertTrue(ex.getMessage().contains("not found"));
    }



    // DELETE TODO NOT FOUND

    @Test
    void testDeleteTodo_NotFound() {

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () ->
                service.deleteTodo(1L)
        );

        assertTrue(ex.getMessage().contains("not found"));
    }


    // UPDATE TODO
    @Test
    void testUpdateTodo() {

        Todo existing = new Todo();
        existing.setId(1L);
        existing.setTitle("Old");
        existing.setStatus(TodoStatus.PENDING);

        Todo updated = new Todo();
        updated.setId(1L);
        updated.setTitle("New");
        updated.setStatus(TodoStatus.COMPLETED);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existing));

        Mockito.when(repository.save(Mockito.any(Todo.class)))
                .thenReturn(updated);

        TodoDTO dto = new TodoDTO();
        dto.setTitle("New");
        dto.setStatus(TodoStatus.COMPLETED.name());

        TodoDTO result = service.updateTodo(1L, dto);

        assertEquals("New", result.getTitle());

        Mockito.verify(repository).save(Mockito.any(Todo.class));
    }

    // UPDATE TODO NOT FOUND

    @Test
    void testUpdateTodo_NotFound() {

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () ->
                service.updateTodo(1L, new TodoDTO())
        );

        assertTrue(ex.getMessage().contains("not found"));
    }
}