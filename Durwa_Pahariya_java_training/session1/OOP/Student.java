package Durwa_Pahariya_java_training.session1.OOP;
public class Student {
    private String name;
    private int rollNumber;
    private double marks;

    // Parameterised Constructor
    public Student(String name, int rollNumber, double marks) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.marks = marks;
    }

    // Getters (Encapsulation)
    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public double getMarks() {
        return marks;
    }

    // Setter
    public void setMarks(double marks) {
        if (marks >= 0) {
            this.marks = marks;
        }
    }

    public void displayDetails() {
        System.out.println("Name: " + name);
        System.out.println("Roll No: " + rollNumber);
        System.out.println("Marks: " + marks);
    }
}
    

