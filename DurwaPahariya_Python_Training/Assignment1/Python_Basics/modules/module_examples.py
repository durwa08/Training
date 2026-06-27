"""
Modules
"""

import math
import random
from calculator import add_numbers


# Question 22: Use math module to find square root, power, and factorial.
def demonstrate_math_module(
        number: int
) -> None:
    """
    Demonstrate commonly used
    functions from the math module.
    """

    # Negative numbers are not suitable for
    # the operations used below, so we
    # check the input before proceeding.
    if number < 0:
        print("Please enter a non-negative number.")
        return

    print(
        f"Square Root: "
        f"{math.sqrt(number)}"
    )

    # math.pow() is used here because the
    # question requires demonstrating the
    # use of functions from the math module.
    print(
        f"Power (number²): "
        f"{math.pow(number, 2)}"
    )

    # factorial() returns the product of
    # all positive integers up to the
    # given number.
    print(
        f"Factorial: "
        f"{math.factorial(number)}"
    )


# Question 23: Generate random numbers using random module.
def generate_random_numbers() -> None:
    """
    Generate random numbers using
    the random module.
    """
    print("\n--- Random Numbers ---")

    # Generating a few random values helps
    # show that different numbers can be
    # produced each time the program runs.
    for _ in range(5):
        print(
            random.randint(1, 100)
        )


# Question 24: Create your own module and import it.
def demonstrate_custom_module(
        first_number: int,
        second_number: int
) -> None:
    """
    Demonstrate the use of a
    custom module.
    """

    # The addition function is imported
    # from another file to demonstrate
    # how custom modules can be reused.
    result = add_numbers(
        first_number,
        second_number
    )

    print(f"Result: {result}")


if __name__ == "__main__":

    print("\n--- Math Module Examples ---")

    number = int(
        input("Enter a number: ")
    )
    demonstrate_math_module(number)

    generate_random_numbers()

    print("\n--- Custom Module Example ---")

    first_number = int(
        input("Enter first number: ")
    )

    second_number = int(
        input("Enter second number: ")
    )

    demonstrate_custom_module(
        first_number,
        second_number )