"""
File Handling
"""


# Question 35: Create a file and write your name into it.
def write_name_to_file(
        file_name: str,
        name: str
) -> None:
    """
    Create a file and write a name into it.
    """

    # Opening the file in write mode creates
    # the file if it does not already exist.
    with open(file_name, "w") as file:
        file.write(name)


# Question 36: Read a file and count words, lines and characters.
def count_file_contents(
        file_name: str
) -> tuple[int, int, int]:
    """
    Count lines, words,
    and characters in a file.
    """
    with open(file_name, "r") as file:
        content = file.read()

    # The file content is analyzed to get
    # the total number of lines, words,
    # and characters.
    line_count = len(content.splitlines())
    word_count = len(content.split())
    character_count = len(content)

    return (
        line_count,
        word_count,
        character_count
    )


# Question 37: Append data to existing file.
def append_to_file(
        file_name: str,
        content: str
) -> None:
    """
    Append content to an existing file.
    """

    # Append mode adds new content at the
    # end of the file without removing
    # the existing data.
    with open(file_name, "a") as file:
        file.write(content)


# Question 38: Copy content from one file to another.
def copy_file_content(
        source_file: str,
        destination_file: str
) -> None:
    """
    Copy content from one file
    to another.
    """

    # Read the content from the source file.
    with open(source_file, "r") as source:
        content = source.read()

    # Write the same content into
    # the destination file.
    with open(destination_file, "w") as destination:
        destination.write(content)


# Question 39: Search a word in a file.
def search_word_in_file(
        file_name: str,
        word: str
) -> bool:
    """
    Search for a word in a file.
    """
    with open(file_name, "r") as file:
        content = file.read()

    # Returns True if the word exists
    # anywhere in the file content.
    return word in content


if __name__ == "__main__":

    print("\n--- Write Name To File ---")

    write_name_to_file(
        "sample.txt",
        "Hello, my name is Durwa."
    )

    print("Name written successfully.")

    print("\n--- Append To File ---")

    append_to_file(
        "sample.txt",
        "\nI am learning Python programming."
    )

    print("Content appended successfully.")

    print("\n--- Count File Contents ---")

    line_count, word_count, character_count = (
        count_file_contents("sample.txt")
    )

    print(f"Lines: {line_count}")
    print(f"Words: {word_count}")
    print(f"Characters: {character_count}")

    print("\n--- Copy File Content ---")

    copy_file_content(
        "sample.txt",
        "copy_sample.txt"
    )

    print("File copied successfully.")

    print("\n--- Search Word In File ---")

    word_found = search_word_in_file(
        "sample.txt",
        "Python"
    )

    print(f"Word Found: {word_found}")