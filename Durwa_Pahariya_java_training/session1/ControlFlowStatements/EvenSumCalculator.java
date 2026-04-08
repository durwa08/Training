package Durwa_Pahariya_java_training.session1.ControlFlowStatements;
public class EvenSumCalculator {
    public int calculateSum() {
        int sum = 0;
        int i = 1;
        while (i <= 10) {
            if (i % 2 == 0) {
                sum += i;
            }
            i++;
        }

        return sum;
    }

    public static void main(String[] args) {
        EvenSumCalculator obj = new EvenSumCalculator();
        System.out.println("Sum of even numbers (1-10): " + obj.calculateSum());
    }
}