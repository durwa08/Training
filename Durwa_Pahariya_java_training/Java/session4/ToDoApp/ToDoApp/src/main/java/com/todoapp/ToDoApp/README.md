# Todo Application (Spring Boot)

## Overview
This is a simple Todo application built using Spring Boot.  
The main purpose of this project is to understand how a backend application is structured using a layered architecture and how different components interact with each other.

The project covers basic CRUD operations and follows clean coding practices as per the assignment guidelines.

---

## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- H2 Database (in-memory)
- Maven

---

## Project Structure

com.todoapp.ToDoApp  
├── controller → Handles API requests  
├── service → Contains business logic  
├── repository → Handles database operations  
├── entity → Database entities  
├── dto → Data transfer objects  
├── mapper → Converts DTO ↔ Entity  
├── exception → Handles errors

---

## Features

1. Create Todo
    - Endpoint: POST /todos
    - Adds a new task
    - createdAt is set automatically
    - Default status is PENDING

2. Get All Todos
    - Endpoint: GET /todos

3. Get Todo by ID
    - Endpoint: GET /todos/{id}
    - Shows error if not found

4. Update Todo
    - Endpoint: PUT /todos/{id}
    - Updates title, description, and status

5. Delete Todo
    - Endpoint: DELETE /todos/{id}

---

## Entity Details

Todo contains:
- id (Primary Key)
- title
- description
- status (PENDING / COMPLETED)
- createdAt

---

## DTO

TodoDTO is used instead of exposing the entity directly.  
Validation is applied:
- title → cannot be null and must have at least 3 characters

---

## Mapping

Manual mapping is done using TodoMapper class.  
This keeps the service layer clean and separates responsibilities.

---

## Exception Handling

- Custom exception: ResourceNotFoundException
- Global exception handler is used to return proper error messages

---

## Configuration

application.properties:

spring.datasource.url=jdbc:h2:mem:testdb  
spring.jpa.hibernate.ddl-auto=update  
spring.h2.console.enabled=true

---

## How to Run

1. Clone the repository
2. Open in IntelliJ
3. Run the main class (ToDoAppApplication)
4. Use Postman to test APIs

---

## Sample Request

POST /todos

{
"title": "Study",
"description": "Spring Boot",
"status": "PENDING"
}

---

## What I Learned

- How Spring Boot manages objects using IoC
- Constructor-based dependency injection
- Difference between Entity and DTO
- How to structure a backend project properly
- Basics of JPA and Hibernate

---

## Conclusion

This project helped me understand how to build a clean and structured backend application using Spring Boot.  
I focused on following best practices like layered architecture, DTO usage, and proper exception handling.