package fei.tuke.sk;

import javax.swing.plaf.synth.SynthUI;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            System.out.print("Enter an expression (e.g., 1+2*3): ");
            Lexer lexer = new Lexer(reader);
            int result = new Parser(lexer).statement();
            System.out.println(result);
        } catch (CalculatorException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}