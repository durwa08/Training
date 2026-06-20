"""
Data Structures - Dictionary
"""


# Question 32: Create a student dictionary and access values.
def access_student_details(
        student: dict[str, str | int]
) -> None:
    """
    Access dictionary values using keys.
    """

    # Accessing values using keys allows us
    # to retrieve specific information from
    # the student dictionary.
    print(f"Name: {student['name']}")
    print(f"Age: {student['age']}")
    print(f"Course: {student['course']}")


# Question 33: Count frequency of characters in a string using dictionary.
def count_character_frequency(
        text: str
) -> dict[str, int]:
    """
    Count frequency of characters
    in a string.
    """

    # Converting to lowercase ensures that
    # uppercase and lowercase letters are
    # counted as the same character.
    text = text.lower()

    frequency = {}

    # A dictionary is useful here because it
    # lets us store each character along with
    # the number of times it appears.
    for character in text:
        frequency[character] = (
                frequency.get(character, 0) + 1
        )

    return frequency


# Question 34: Merge two dictionaries.
def merge_dictionaries(
        first_dictionary: dict,
        second_dictionary: dict
) -> dict:
    """
    Merge two dictionaries.
    """

    # The union operator combines the
    # key-value pairs from both dictionaries
    # into a single dictionary.
    return first_dictionary | second_dictionary


if __name__ == "__main__":

    print("\n--- Student Dictionary ---")

    student = {
        "name": "Durwa",
        "age": 22,
        "course": "Python"
    }

    access_student_details(student)

    print("\n--- Character Frequency ---")

    text = "python"
    frequency = count_character_frequency(text)

    print(f"String: {text}")
    print(f"Frequency: {frequency}")

    print("\n--- Merge Dictionaries ---")

    first_dictionary = {"name": "Durwa"}
    second_dictionary = {"course": "Python"}

    merged_dictionary = merge_dictionaries(
        first_dictionary,
        second_dictionary
    )

    print(f"First Dictionary: {first_dictionary}")
    print(f"Second Dictionary: {second_dictionary}")
    print(f"Merged Dictionary: {merged_dictionary}")