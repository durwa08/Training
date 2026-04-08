package Durwa_Pahariya_java_training.session1.ControlFlowStatements;
import java.util.*;
public class LargestNumber {
    public int findLargest(int a, int b, int c) {
        if (a >= b && a >= c) {
            return a;
        } else if (b >= c) {
            return b;
        } else {
            return c;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LargestNumber obj = new LargestNumber();
        System.out.print("Enter three numbers: ");
        int a = sc.nextInt();
        int b = sc.nextInt();
        int c = sc.nextInt();
         System.out.println("Largest: " + obj.findLargest(a, b, c));

        sc.close();
    }
}
