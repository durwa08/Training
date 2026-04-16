# Spring Boot REST Assignment – Session 3

## Overview

This project is a Spring Boot application developed as part of the Session 3 assignment.  
The main goal of this assignment was to understand how REST APIs work in Spring Boot and how to structure a project properly using layered architecture.

The application manages a list of users (stored in memory) and provides APIs to:
- Search users based on different parameters
- Submit structured data
- Delete users with a confirmation check

No database is used in this project. All data is stored in an in-memory list for simplicity.

---

## Tech Stack

- Java 17
- Spring Boot
- Maven
- Postman (for API testing)

---

## Project Structure

The project follows a clean layered architecture:

com.spring.AdvanceTraining

- controller → Handles incoming HTTP requests and sends responses
- service → Contains business logic and processing
- repository → Stores and manages data (in-memory list)
- model → Contains data classes like User and SubmissionRequest
- exception → Handles global exceptions
- main class → Entry point of the application

This separation helps keep the code clean and easy to understand.

---

## Features Implemented
### 1. User Search API

**Endpoint:**  
GET /users/search

This API allows searching users based on optional query parameters.

**Parameters:**
- name (String)
- age (Integer)
- role (String)

**Behavior:**
- If no parameters are provided → returns all users
- If parameters are provided → filters users accordingly
- Name and role matching is case-insensitive
- Age matching is exact

**Examples:**
- /users/search
- /users/search?name=Priya
- /users/search?age=30
- /users/search?role=USER
- /users/search?age=30&role=USER

---

### 2. Submit Data API

**Endpoint:**  
POST /users/submit

This API is used to submit structured data.

**Request Body:**
```json
{
  "data": "Sample input"
}