package Durwa_Pahariya_java_training.session1.Advanced;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderDemo {

    public void readFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            System.out.println("Error in reading file");
        }
    }

    public static void main(String[] args) {
        FileReaderDemo obj = new FileReaderDemo();
        obj.readFile("sample.txt"); 
    }
}