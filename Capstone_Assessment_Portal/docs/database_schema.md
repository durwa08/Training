# Assessment Portal Database Schema

## Users Collection

```json
{
  "_id": "ObjectId",
  "name": "Durwa",
  "email": "durwa08@gmail.com",
  "password": "hashed_password",
  "role": "admin",
  "created_at": "datetime"
}
```

## Categories Collection

```json
{
  "_id": "ObjectId",
  "name": "Java",
  "description": "Java related quizzes"
}
```

## Quizzes Collection

```json
{
  "_id": "ObjectId",
  "title": "Java Basics",
  "category_id": "ObjectId",
  "time_limit": 30,
  "pass_percentage": 40
}
```

## Questions Collection

```json
{
  "_id": "ObjectId",
  "quiz_id": "ObjectId",
  "question_text": "What is JVM?",
  "question_type": "MCQ",
  "options": [
    "Option A",
    "Option B",
    "Option C",
    "Option D"
  ],
  "correct_answer": "Option A",
  "difficulty": "easy"
}
```

## Attempts Collection

```json
{
  "_id": "ObjectId",
  "user_id": "ObjectId",
  "quiz_id": "ObjectId",
  "answers": [],
  "status": "IN_PROGRESS"
}
```

## Results Collection

```json
{
  "_id": "ObjectId",
  "attempt_id": "ObjectId",
  "score": 8,
  "percentage": 80,
  "status": "PASS"
}
```