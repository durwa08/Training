package Durwa_Pahariya_java_training.session1.DataTypesAndOperators;
import java.util.*;
public class Operator {
   public void arithmeticOps(int a, int b) {
        System.out.println("Arithmetic Operators:");
        System.out.println("Sum: " + (a + b));
        System.out.println("Sub: " + (a - b));
        System.out.println("Mul: " + (a * b));
        System.out.println("Div: " + (a / b));
    }

    public void relationalOps(int a, int b) {
        System.out.println("Relational Operators:");
        System.out.println(a > b);
        System.out.println(a < b);
        System.out.println(a == b);
    }

    public void logicalOps(boolean x, boolean y) {
        System.out.println("Logical Operators:");
        System.out.println(x && y);
        System.out.println(x || y);
        System.out.println(!x);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Operator op = new Operator();

        System.out.print("Enter two numbers: ");
        int a = sc.nextInt();
        int b = sc.nextInt();

        System.out.print("Enter two boolean values: ");
        boolean x = sc.nextBoolean();
        boolean y = sc.nextBoolean();

        op.arithmeticOps(a, b);
        op.relationalOps(a, b);
        op.logicalOps(x, y);

        sc.close();
    }
}