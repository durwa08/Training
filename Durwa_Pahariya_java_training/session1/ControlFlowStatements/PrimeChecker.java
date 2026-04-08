package Durwa_Pahariya_java_training.session1.ControlFlowStatements;
import java.util.*;
public class PrimeChecker {
     public boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PrimeChecker checker = new PrimeChecker();

        System.out.print("Enter number: ");
        int num = sc.nextInt();

        if (checker.isPrime(num)) {
            System.out.println("Prime Number");
        } else {
            System.out.println("Not Prime");
        }

        sc.close();
    }

    //Runs in O(√n) instead of O(n)
}
