import java.util.Scanner;

public class FibonacciSeries {
    public void printFibonacci(int limit) {
        int a = 0, b = 1;

        System.out.print("Fibonacci: " + a + " " + b + " ");

        for (int i = 2; i < limit; i++) {
            int c = a + b;
            System.out.print(c + " ");
            a = b;
            b = c;
        }
    }
     public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FibonacciSeries series = new FibonacciSeries();

        System.out.print("Enter terms needed : ");
        int n = sc.nextInt();

        series.printFibonacci(n);

        sc.close();
    }
}
    

