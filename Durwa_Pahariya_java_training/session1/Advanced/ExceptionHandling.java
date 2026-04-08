package Durwa_Pahariya_java_training.session1.Advanced;

public class ExceptionHandling {
    public void divide(int a, int b) {
        try {
            int result = a / b;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Error: Cannot divide by zero");
        } finally {
            System.out.println("Execution completed");
        }
    }

    public static void main(String[] args) {
        ExceptionHandling obj = new ExceptionHandling();
        obj.divide(10, 0);
    }
    
}
