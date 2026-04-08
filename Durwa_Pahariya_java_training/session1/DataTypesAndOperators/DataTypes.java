package Durwa_Pahariya_java_training.session1.DataTypesAndOperators;

/*Primitive Data Types stores Actual Values they are faster,and memory efficient
Examples are : int,double,char,boolean.
While Refrence DataTypes stores memory address and are used for objects
Examples : String,Arrays,Classes.
*/

public class DataTypes {
    public void showPrimitive() {
        int number = 100;
        double price = 199.99;
        char grade = 'A';
        boolean isValid = true;

        System.out.println("Primitive Data Types:");
        System.out.println(number + ", " + price + ", " + grade + ", " + isValid);
    }
    //Refrence Datatype-->Derived from primitives

    public void showReference() {
        String name = "Durwa";
        int[] arr = {1, 12, 3};

        System.out.println("Reference Data Types:");
        System.out.println("Name: " + name);
        System.out.println("Array Second element: " + arr[1]);
    }

    public static void main(String[] args) {
        DataTypes demo = new DataTypes();
        demo.showPrimitive();
        demo.showReference();
    }
    
}
