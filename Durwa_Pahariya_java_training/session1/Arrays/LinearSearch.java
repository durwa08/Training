package Durwa_Pahariya_java_training.session1.Arrays;
import java.util.*;
public class LinearSearch {
    public int search(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i; 
            }
        }
        return -1;
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
        
        return arr;
        
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LinearSearch obj = new LinearSearch();

        int[] arr = obj.takeInput();

        System.out.print("Enter element to search: ");
        int target = sc.nextInt();

        int result = obj.search(arr, target);

        if (result != -1) {
            System.out.println("Element found at index: " + result);
        } else {
            System.out.println("Element not found");
        }
        sc.close();
    }
    
}
