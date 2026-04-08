package Durwa_Pahariya_java_training.session1.StringManipulation;
import java.util.Scanner;
public class StringReverse {
    
    public String reverse(String input) {
        StringBuilder reversed = new StringBuilder(input);
        return reversed.reverse().toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringReverse obj = new StringReverse();

        System.out.print("Enter string: ");
        String input = sc.nextLine();

        System.out.println("Reversed: " + obj.reverse(input));

        sc.close();
    }
}

