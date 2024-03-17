package fei.tuke.sk;
import java.io.IOException;
import java.io.Reader;


public class Lexer {
    private final Reader input;
    private int current;
    private int value;

    public Lexer(Reader reader) {
        this.input = reader;
        try {
            consume();
        } catch (CalculatorException e) {
            throw new CalculatorException("Error reading input", e);
        }
    }

    public Token nextToken() {
        try {
            switch (current) {
                case '+':
                    consume();
                    return Token.PLUS;
                case '-':
                    consume();
                    return Token.MINUS;
                case '*':
                    consume();
                    return Token.MUL;
                case '/':
                    consume();
                    return Token.DIV;
                case '(':
                    consume();
                    return Token.LPAR;
                case ')':
                    consume();
                    return Token.RPAR;
                case '^':
                    consume();
                    return Token.EXP;
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                    if (Character.isDigit((char) current)) {
                        value = current - '0';
                        while (Character.isDigit((char) (current = input.read()))) {
                            value = value * 10 + (current - '0');
                        }
                        return Token.NUMBER;
                    } else {
                        throw new CalculatorException("Invalid character: " + (char) current);
                    }
                default:
                    return Token.EOF;
            }
        }catch (IOException e){
            throw new CalculatorException("Error reading input", e);
        }
    }


    private void consume() {
        try {
            current = input.read();
        } catch (Exception e) {
            throw new RuntimeException("Error reading input", e);
        }
    }

    public int getValue(){
        return value;
    }
}