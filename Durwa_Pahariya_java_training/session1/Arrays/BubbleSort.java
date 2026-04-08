package Durwa_Pahariya_java_training.session1.Arrays;
import java.util.*;
public class BubbleSort {
    public void sort(int[] arr) {
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // swap
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }

            if (!swapped) break; // optimization
        }
    }

    public void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
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
        BubbleSort obj = new BubbleSort();

        int[] arr = obj.takeInput();
        obj.sort(arr);

        System.out.print("Sorted array: ");
        obj.printArray(arr);
    }
    
}
