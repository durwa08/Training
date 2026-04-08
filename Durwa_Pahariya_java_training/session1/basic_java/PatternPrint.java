public class PatternPrint {
    
    public void printRtTriangle(int rows){
        for(int i =1;i <= rows; i++){
            for(int j = 1;j <=i ;j++){
                System.out.print("* ");
            }
            System.out.println();
        }

    }
    public static void main(String[] args) {
        PatternPrint print = new PatternPrint();
        print.printRtTriangle(3);
    }
}
