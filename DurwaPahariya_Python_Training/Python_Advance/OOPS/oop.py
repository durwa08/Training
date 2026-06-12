"""
Object-Oriented Programming (OOP)
"""


# Question 40: Create a Student class with attributes and display details.
class Student:
    """
    Stores basic information about a student like name,age course.
    """

    def __init__(
            self,
            name: str,
            age: int,
            course: str
    ):
        self.name = name
        self.age = age
        self.course = course

    def display_details(self) -> None:
        """
        Display student information.
        """
        print(f"Name: {self.name}")
        print(f"Age: {self.age}")
        print(f"Course: {self.course}")


# Question 41: Create a Car class with a constructor.
class Car:
    """
    Represents a car with a brand and model.
    """

    def __init__(
            self,
            brand: str,
            model: str
    ):
        self.brand = brand
        self.model = model

    def display_car_details(self) -> None:
        """
        Display car information.
        """
        print(f"Brand: {self.brand}")
        print(f"Model: {self.model}")


# Question 42: Implement inheritance using Person and Employee class.
class Person:
    """
    Base class that stores a person's name.
    """

    def __init__(
            self,
            name: str
    ):
        self.name = name


class Employee(Person):
    """
    Employee inherits the name attribute
    from the Person class and adds an ID.
    """

    def __init__(
            self,
            name: str,
            employee_id: int
    ):
        # Calling the parent constructor
        # avoids repeating the code used
        # to initialize the name attribute.
        super().__init__(name)
        self.employee_id = employee_id

    def display_employee_details(self) -> None:
        """
        Display employee information.
        """
        print(f"Name: {self.name}")
        print(f"Employee ID: {self.employee_id}")


# Question 43: Implement encapsulation using private variables in Bank class.
class Bank:
    """
    Represents a bank account with
    a private balance.
    """

    def __init__(
            self,
            balance: float
    ):
        # The balance is kept private so
        # it cannot be modified directly
        # from outside the class.
        self.__balance = balance

    def deposit(
            self,
            amount: float
    ) -> None:
        """
        Add money to the account.
        """
        self.__balance += amount

    def get_balance(self) -> float:
        """
        Return the current account balance.
        """
        return self.__balance


# Question 44: Demonstrate polymorphism using different classes with the same method name.
class Dog:
    """
    Represents a dog.
    """

    def make_sound(self) -> None:
        print("Bark")


class Cat:
    """
    Represents a cat.
    """

    def make_sound(self) -> None:
        print("Meow")


if __name__ == "__main__":

    print("\n--- Student Class ---")

    student = Student(
        "Durwa",
        22,
        "Python"
    )
    student.display_details()

    print("\n--- Car Class ---")

    car = Car(
        "Toyota",
        "Camry"
    )
    car.display_car_details()

    print("\n--- Inheritance ---")

    employee = Employee(
        "Rahul",
        101
    )
    employee.display_employee_details()

    print("\n--- Encapsulation ---")

    bank_account = Bank(1000)
    bank_account.deposit(500)

    print(
        f"Balance: "
        f"{bank_account.get_balance()}"
    )

    print("\n--- Polymorphism ---")

    animals = [Dog(), Cat()]

    # Both objects respond to the same
    # method call in their own way.
    for animal in animals:
        animal.make_sound()