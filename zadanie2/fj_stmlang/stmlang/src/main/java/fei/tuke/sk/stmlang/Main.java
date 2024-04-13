package fei.tuke.sk.stmlang;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Main {

    public static void main(String[] args) throws IOException {
        String dslInput = Files.readString(Path.of("C:\\Users\\Rog Strix\\Documents\\FJGIT\\fj-7825\\zadanie2\\fj_stmlang\\stmlang\\src\\main\\resources/input.txt")); // This should be replaced with actual DSL content.
        // If reading from a file, use Files.readString(Path.of("path/to/your.dsl"));

        // Step 1: Tokenization
        Lexer lexer = new Lexer(dslInput);

        // Step 2: Parsing
        Parser parser = new Parser(lexer);
        StateMachineDefinition stateMachine = parser.parse();

        // Step 3: Code Generation
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("C:\\Users\\Rog Strix\\Documents\\FJGIT\\fj-7825\\zadanie2\\fj_stmlang\\stmlang\\src\\files\\output.c"), StandardOpenOption.CREATE)) {
            Generator generator = new Generator(stateMachine, writer);
            generator.generate_code();
            System.out.println("C code successfully generated in output.c");
        } catch (IOException e) {
            System.err.println("An error occurred during file writing.");
        }
    }
}
