package fei.tuke.sk.stmlang;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Lexer {
    private static final Map<String, TokenType> keywords = new HashMap<>();

    static {
        keywords.put("commands", TokenType.COMMANDS);
        keywords.put("resetCommands", TokenType.RESET_COMMANDS);
        keywords.put("events", TokenType.EVENTS);
        keywords.put("state", TokenType.STATE);
        keywords.put("actions", TokenType.ACTIONS);
    }

    private final String input;
    private int readPosition; // Current reading position (after current char)
    private char currentChar; // Current character under examination

    public Lexer(String input) {
        this.input = input + "\n"; // Add an extra newline at the end to simplify the parser
        readPosition = 0;
        advance();
    }

    public Token nextToken() {
        skipWhitespace();
        Token token = null;

        switch (currentChar) {
            case '\0': // End of file
                token = new Token(TokenType.EOF, "");
                break;
            case '{':
                token = new Token(TokenType.LBRACE, "{");
                break;
            case '}':
                token = new Token(TokenType.RBRACE, "}");
                break;
            case '-':
                if (peek() == '-') {
                    advance(); // Advance past '-'
                    advance(); // Advance past '>'
                    token = new Token(TokenType.ARROW, "-->");
                }
                break;
            case '=':
                token = new Token(TokenType.ASSIGN, "=");
                break;
            case '[':
                token = new Token(TokenType.LBRACKET, "[");
                break;
            case ']':
                token = new Token(TokenType.RBRACKET, "]");
                break;
            case '\'':
                token = handleCharLiteral();
                break;
            default:
                if (isLetter(currentChar) || isDigit(currentChar)) {
                    String lexeme = readWhile(ch -> isLetter(ch) || isDigit(ch) || ch == '_');
                    TokenType type = keywords.getOrDefault(lexeme, TokenType.IDENTIFIER);
                    token = new Token(type, lexeme);
                    return token; // Early return to avoid advancing twice
                }
                break;
        }

        advance(); // Ensure to advance after handling the token
        return token;
    }

    private Token handleCharLiteral() {
        advance(); // Move past the opening quote.
        StringBuilder charValue = new StringBuilder();
        while (currentChar != '\'' && currentChar != '\0') {
            charValue.append(currentChar);
            advance();
        }
        if (currentChar == '\'') { // Ensure closing quote is present.
            advance(); // Move past the closing quote.
            return new Token(TokenType.CHAR_LITERAL, charValue.toString());
        } else {
            throw new RuntimeException("Unterminated character literal");
        }
    }


    private void skipWhitespace() {
        while (currentChar == ' ' || currentChar == '\t' || currentChar == '\r' || currentChar == '\n') {
            advance();
        }
    }

    private String readWhile(Predicate<Character> condition) {
        StringBuilder builder = new StringBuilder();
        while (condition.test(currentChar)) {
            builder.append(currentChar);
            advance();
        }
        return builder.toString();
    }

    private boolean isLetter(char ch) {
        return Character.isLetter(ch) || ch == '_';
    }

    private boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }

    private void advance() {
        currentChar = readPosition >= input.length() ? '\0' : input.charAt(readPosition);
        readPosition++;
    }

    private char peek() {
        return readPosition >= input.length() ? '\0' : input.charAt(readPosition);
    }

}
