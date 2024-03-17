package fei.tuke.sk;

import java.io.IOException;

public class Parser {
    private final Lexer lexer;
    private Token token;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        consume();
    }

    public int statement() {
        try {
            int result = expr();
            match(Token.EOF);
            return result;
        } catch (IOException e) {
            throw new CalculatorException("Error parsing input", e);
        }
    }

    private int expr() throws IOException {
        int value = mul();

        while (token == Token.PLUS || token == Token.MINUS) {
            Token op = token;
            consume();

            if (op == Token.PLUS) {
                value += mul();
            } else {
                value -= mul();
            }
        }

        return value;
    }

    private int mul() throws IOException {
        int value = exp();

        while (token == Token.MUL || token == Token.DIV) {
            Token op = token;
            consume();

            if (op == Token.MUL) {
                value *= exp();
            } else {
                value /= exp();
            }
        }

        return value;
    }

    private int exp() throws IOException {
        int value = term();

        if (token == Token.EXP) {
            consume();
            value = (int) Math.pow(value, exp());
        }

        return value;
    }

    private int term() throws IOException {
        int value;

        if (token == Token.LPAR) {
            consume();
            value = expr();
            match(Token.RPAR);
        } else if (token == Token.NUMBER) {
            value = lexer.getValue();
            consume();
        } else if (token == Token.MINUS) {
            consume();
            value = -term();
        }else if(token == Token.PLUS){
            consume();
            value = term();
        }else {
            throw new CalculatorException("Expected number or left parenthesis, but found " + token);
        }

        return value;
    }




    private void match(Token expectedSymbol) throws IOException {
        if (token == expectedSymbol) {
            consume();
        } else {
            throw new CalculatorException("Expected " + expectedSymbol + " but found " + token);
        }
    }

    private void consume() {
        token = lexer.nextToken();
    }

}
