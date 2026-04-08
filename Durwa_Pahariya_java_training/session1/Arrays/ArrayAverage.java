package Durwa_Pahariya_java_training.session1.Arrays;
import java.util.*;
public class ArrayAverage {
    public double calculateAverage(int[] arr) {
        int sum = 0;

        for (int num : arr) {
            sum += num;
        }

        return (double) sum / arr.length;
    }

    public int[] takeInput() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter size: ");
        int size = sc.nextInt();

        int[] arr = new int[size];

        System.out.println("Enter elements:");
        for (int i = 0; i < size; i++) {
            arr[i] = sc.nextInt();
        }
        sc.close();
        return arr;
    }

    public static void main(String[] args) {
        ArrayAverage obj = new ArrayAverage();

        int[] arr = obj.takeInput();
        System.out.println("Average: " + obj.calculateAverage(arr));
        
    }

}
