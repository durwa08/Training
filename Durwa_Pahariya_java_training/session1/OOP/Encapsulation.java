package Durwa_Pahariya_java_training.session1.OOP;

public class Encapsulation {
    //Encapsulation is 1 of 4 pillars of oops.
    
    /*
    Encapsulation is the process of wrapping up of data and methods inside a single unit called class
    Encapsulation = Wrapping data + methods together and restricting direct access 
    it is done using private variables and getters/setters
    
    Advantage of Encapsulation:
    Data Hiding and security.
    Better control over data.
    Code becomes more maintainable.

    Example is as follows:

    */
    class Student {
    
    private double marks;

    // Getter
    public double getMarks() {
        return marks;
    }

    // Setter
    public void setMarks(double marks) {
        this.marks = marks;
    }
    }
    
    }
