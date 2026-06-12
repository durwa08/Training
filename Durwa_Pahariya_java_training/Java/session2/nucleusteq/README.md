#  Java Fundamentals - Spring Boot Assignment

##  Project Overview
This project is developed as part of the Java Fundamentals Assignment.  
It demonstrates the use of Spring Boot, REST APIs, and layered architecture with proper implementation of core Spring concepts.

---

##  Tech Stack
- Java 17  
- Spring Boot  
- Maven  
- REST APIs  

---

##  Project Structure

controller → Handles API requests  
service → Contains business logic  
repository → Manages data (in-memory)  
model → Defines data structure  
component → Reusable helper classes  
exception → Global exception handling  

---

## ⚙️ Features Implemented

###  User Management System
- GET /users → Fetch all users  
- POST /users → Create a new user  
- GET /users/{id} → Fetch user by ID  

---

###  Notification System
- GET /notify → Triggers notification  
- Returns: "Notification sent"  

✔ Uses @Component for reusable logic  
✔ Service calls component (Separation of concerns)

---

###  Dynamic Message Formatter
- GET /message?type=SHORT/LONG  

Returns:  
- SHORT → "Short Message"  
- LONG → "This is a detailed long message format"  

✔ Uses multiple components  
✔ Runtime decision using Map injection  
✔ No if-else in controller  

---

##  Concepts Covered

- IoC (Inversion of Control)  
- Dependency Injection (Constructor-based)  
- Component Scanning  
- Layered Architecture  
- Separation of Concerns  
- Exception Handling  

---

##  Data Handling
- Uses in-memory data structure (ArrayList)  
- No external database required  

---

##  Exception Handling
- Global exception handler implemented using @RestControllerAdvice  
- Handles runtime errors gracefully  

---

##  How to Run

1. Clone the repository  
   git clone <your-repo-link>  

2. Navigate to project folder  
   cd java/session2  

3. Run the application  
   mvn spring-boot:run  

4. Test APIs using Postman  

---

##  Sample Request (POST /users)

{
  "id": 1,
  "name": "Durwa",
  "email": "durwa@gmail.com"
}

---

##  Commit Strategy
- Minimum 10 commits followed  
- Each commit represents logical progress  
- Clean and readable commit history maintained  

---

##  Notes
This project is built through a combination of live training sessions and self-learning, focusing on understanding core concepts and clean coding practices.
