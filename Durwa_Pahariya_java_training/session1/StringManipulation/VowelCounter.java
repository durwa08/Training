package Durwa_Pahariya_java_training.session1.StringManipulation;
import java.util.Scanner;
public class VowelCounter {
    public int countVowels(String input) {
        int count = 0;

        input = input.toLowerCase();

        for (char ch : input.toCharArray()) {
            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        VowelCounter obj = new VowelCounter();

        System.out.print("Enter string: ");
        String input = sc.nextLine();

        System.out.println("Vowel count: " + obj.countVowels(input));

        sc.close();
    }
}