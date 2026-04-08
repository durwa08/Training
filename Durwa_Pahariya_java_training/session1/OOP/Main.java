package Durwa_Pahariya_java_training.session1.OOP;

public class Main {
    public static void main(String[] args) {

        Student s1 = new Student("Durwa", 82, 91.5);
        s1.displayDetails();

        System.out.println("-----");

        GraduateStudent g1 = new GraduateStudent("Anji", 12, 90.0, "AIML");
        g1.displayDetails();
    }

}
