package com.todoapp.ToDoApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.ToDoApp.dto.TodoDTO;
import com.todoapp.ToDoApp.service.TodoService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService service;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ CREATE TODO TEST
    @Test
    void testCreateTodo() throws Exception {

        TodoDTO dto = new TodoDTO();
        dto.setTitle("Study");
        dto.setDescription("Spring Boot");
        dto.setStatus("PENDING");

        Mockito.when(service.createTodo(Mockito.any()))
                .thenReturn(dto);

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Study"));
    }

    // ✅ GET ALL TODOS
    @Test
    void testGetAllTodos() throws Exception {

        TodoDTO dto = new TodoDTO();
        dto.setTitle("Task1");
        dto.setStatus("PENDING");

        Mockito.when(service.getAllTodos())
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    // ✅ GET BY ID
    @Test
    void testGetTodoById() throws Exception {

        TodoDTO dto = new TodoDTO();
        dto.setTitle("Task1");

        Mockito.when(service.getTodoById(1L))
                .thenReturn(dto);

        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task1"));
    }

    // ✅ UPDATE TODO
    @Test
    void testUpdateTodo() throws Exception {

        TodoDTO dto = new TodoDTO();
        dto.setTitle("Updated Task");

        Mockito.when(service.updateTodo(Mockito.eq(1L), Mockito.any()))
                .thenReturn(dto);

        mockMvc.perform(put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    //  DELETE TODO
    @Test
    void testDeleteTodo() throws Exception {

        Mockito.doNothing().when(service).deleteTodo(1L);

        mockMvc.perform(delete("/todos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted successfully"));
    }
}