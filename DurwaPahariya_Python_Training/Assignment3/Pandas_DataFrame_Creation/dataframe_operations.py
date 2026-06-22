"""
Assignment 2 - Pandas DataFrame Creation
"""

import pandas as pd

BONUS_RATE = 0.10


# -----------------------------------
# Question 1: Create DataFrame
# -----------------------------------

employee_records = {
    "Name": ["Rahul", "Priya", "Amit", "Anuj"],
    "Age": [25, 30, 28, 35],
    "Department": ["HR", "IT", "Finance", "IT"],
    "Salary": [30000, 50000, 45000, 60000]
}

employees_df = pd.DataFrame(employee_records)

print("Question 1: Employee Data")
print(employees_df)


# -----------------------------------
# Question 2: Display First 2 Rows
# -----------------------------------

print("\nQuestion 2: First Two Records")
print(employees_df.head(2))


