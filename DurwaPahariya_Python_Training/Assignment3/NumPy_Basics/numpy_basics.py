"""
Assignment 1 - NumPy Fundamentals
"""

import numpy as np


# -----------------------------------
# Question 1: Create a NumPy Array
# -----------------------------------

array_values = [5, 15, 25, 35, 45]

numbers = np.array(array_values)

print("Question 1: NumPy Array")
print(numbers)


# -----------------------------------
# Question 2: Find Mean, Maximum,
# Minimum, and Sum
# -----------------------------------

print("\nQuestion 2: Array Statistics")
print("Average:", np.mean(numbers))
print("Maximum:", np.max(numbers))
print("Minimum:", np.min(numbers))
print("Sum:", np.sum(numbers))


