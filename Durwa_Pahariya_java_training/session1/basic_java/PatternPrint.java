import java.util.Scanner;

public class PatternPrint {
    
    public void printRtTriangle(int rows) {
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PatternPrint obj = new PatternPrint();

        System.out.print("Enter number of rows: ");
        int n = sc.nextInt();
        obj.printRtTriangle(n);

        sc.close();
    }
}
