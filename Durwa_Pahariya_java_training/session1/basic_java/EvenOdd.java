import java.util.*;
public class EvenOdd {
    public boolean isEven(int num){
        return num % 2 == 0;

    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EvenOdd check = new EvenOdd();
        
        System.out.println("Enter the number :");
        int num = sc.nextInt();

        if(check.isEven((num))){
            System.out.println("Number is Even");
        }else{
            System.out.println("Number is odd");
        }
        sc.close();

    }

    
}
