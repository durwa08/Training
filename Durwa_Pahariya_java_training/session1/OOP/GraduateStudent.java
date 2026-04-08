package Durwa_Pahariya_java_training.session1.OOP;
// Inheritance is oops principle in which chils class inherits properties and functionalities from parent class
/*
 GraduateStudent extends Student it helps in:
 Code reuse
Clean hierarchy

 */
public class GraduateStudent extends Student {
     private String specialization;

    public GraduateStudent(String name, int rollNumber, double marks, String specialization) {
        super(name, rollNumber, marks);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    // Polymorphism (Method Overriding) Runtime Polymorphism --> Same method, different behavior
    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Specialization: " + specialization);
    }

    
}
