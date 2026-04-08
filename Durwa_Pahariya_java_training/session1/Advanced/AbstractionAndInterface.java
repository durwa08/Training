package Durwa_Pahariya_java_training.session1.Advanced;

// Abstract Class Example
abstract class Animal {
    abstract void sound();  

    void sleep() {
        System.out.println("Animal is sleeping");
    }
}

// Concrete class extending abstract class
class Dog extends Animal {

    @Override
    void sound() {
        System.out.println("Dog barks");
    }
}

// Interface
interface Flyable {
    void fly();
}

// Class implementing interface
class Bird implements Flyable {

    @Override
    public void fly() {
        System.out.println("Bird is flying");
    }
}

public class AbstractionAndInterface {

    public static void main(String[] args) {

        // Abstraction example
        Animal a = new Dog();
        a.sound();
        a.sleep();

        // Interface example
        Flyable f = new Bird();
        f.fly();
    }
}