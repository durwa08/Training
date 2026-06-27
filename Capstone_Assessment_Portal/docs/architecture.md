# Assessment Portal Architecture

## Relationships

Category
│
└── Quiz (One Category can have many Quizzes)
│
└── Question (One Quiz can have many Questions)

User
│
└── Attempt (One User can have many Attempts)
│
└── Result (Each Attempt generates one Result)

## Technology Stack

Backend : FastAPI
Database : MongoDB
Frontend : ReactJS
Authentication : JWT
Testing : Pytest
Version Control : Git & GitHub