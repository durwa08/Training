import java.util.*;
public class FactorialCalculator {
    public long calculateFactorial(int num){
        long result = 1;
        for(int i = 1;i <= num; i++){
            result *=i;

        }
        return result;

    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FactorialCalculator cal = new FactorialCalculator();
        System.out.println("Enter the number : ");
        int num = sc.nextInt();
        System.out.println("Factorial of a given number is : " + cal.calculateFactorial(num));

        sc.close();
    }
}
