"""
Data Structures - List
"""


# Question 25: Create a list of 10 numbers and find sum, max, sort it, and remove duplicates.
def perform_list_operations(
        numbers: list[int]
) -> tuple[int, int, list[int], list[int]]:
    """
    Find sum, maximum value,
    sorted list, and remove duplicates.
    """

    # Built-in functions make it easy to
    # calculate common statistics from a list.
    total = sum(numbers)
    maximum_number = max(numbers)

    # Creating a sorted copy keeps the
    # original list unchanged.
    sorted_numbers = sorted(numbers)

    # Converting the list to a set removes
    # duplicate values automatically.
    unique_numbers = list(set(numbers))

    return (
        total,
        maximum_number,
        sorted_numbers,
        unique_numbers
    )


# Question 26: Count even and odd numbers in a list.
def count_even_odd_numbers(
        numbers: list[int]
) -> tuple[int, int]:
    """
    Count even and odd numbers in a list.
    """
    even_count = 0
    odd_count = 0

    # Checking the remainder when divided
    # by 2 helps determine whether a
    # number is even or odd.
    for number in numbers:
        if number % 2 == 0:
            even_count += 1
        else:
            odd_count += 1

    return even_count, odd_count


# Question 27: Reverse a list without using reverse().
def reverse_list(
        numbers: list[int]
) -> list[int]:
    """
    Reverse a list without using reverse().
    """

    # List slicing with a step of -1
    # returns the elements in reverse order.
    return numbers[::-1]


if __name__ == "__main__":

    print("\n--- List Operations ---")

    numbers = [
        10, 5, 20, 15, 10,
        25, 30, 5, 40, 50
    ]

    total, maximum_number, sorted_numbers, unique_numbers = (
        perform_list_operations(numbers)
    )

    print(f"Original List: {numbers}")
    print(f"Sum: {total}")
    print(f"Maximum Number: {maximum_number}")
    print(f"Sorted List: {sorted_numbers}")
    print(
        f"List After Removing Duplicates: "
        f"{unique_numbers}"
    )

    print("\n--- Even and Odd Count ---")

    even_count, odd_count = (
        count_even_odd_numbers(numbers)
    )

    print(f"Even Numbers Count: {even_count}")
    print(f"Odd Numbers Count: {odd_count}")

    print("\n--- Reverse List ---")

    reversed_numbers = reverse_list(numbers)

    print(f"Original List: {numbers}")
    print(f"Reversed List: {reversed_numbers}")