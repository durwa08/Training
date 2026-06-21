"""
Data Structures - Tuple
"""


# Question 28: Create a tuple and access elements.
def display_tuple_elements(
        tuple_data: tuple
) -> None:
    """
    Access and display tuple elements.
    """

    # Each value in a tuple can be accessed
    # using its index position.
    for index in range(len(tuple_data)):
        print(
            f"Element at index "
            f"{index}: {tuple_data[index]}"
        )


# Question 29: Convert tuple into list and modify it.
def modify_tuple(
        tuple_data: tuple,
        index: int,
        new_value: str | int
) -> list:
    """
    Convert tuple to list and modify an element.
    """

    # Tuples cannot be changed after creation,
    # so we first convert it into a list.
    modified_list = list(tuple_data)

    # Once converted, the required element
    # can be updated using its index.
    modified_list[index] = new_value

    return modified_list


if __name__ == "__main__":

    student_data = (
        "Durwa",
        22,
        "Python"
    )

    print("\n--- Tuple Elements ---")

    display_tuple_elements(student_data)

    print("\n--- Tuple to List Conversion ---")

    modified_student_data = modify_tuple(
        student_data,
        1,
        23
    )

    print(
        f"Original Tuple: "
        f"{student_data}"
    )

    print(
        f"Modified List: "
        f"{modified_student_data}"
    )