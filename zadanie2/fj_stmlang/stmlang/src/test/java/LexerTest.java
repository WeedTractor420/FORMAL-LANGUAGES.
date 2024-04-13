import fei.tuke.sk.stmlang.Lexer;
import fei.tuke.sk.stmlang.Token;
import fei.tuke.sk.stmlang.TokenType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    private Lexer createLexer(String input) {
        return new Lexer(input);
    }

    @Test
    void testLexingBraces() {
        Lexer lexer = createLexer("{ }");
        Token lbrace = lexer.nextToken();
        Token rbrace = lexer.nextToken();

        assertAll("Checking braces",
                () -> assertEquals(TokenType.LBRACE, lbrace.tokenType()),
                () -> assertEquals("{", lbrace.attribute()),
                () -> assertEquals(TokenType.RBRACE, rbrace.tokenType()),
                () -> assertEquals("}", rbrace.attribute())
        );
    }

    @Test
    void testLexingKeywords() {
        Lexer lexer = createLexer("commands resetCommands events state actions");
        Token commands = lexer.nextToken();
        Token resetCommands = lexer.nextToken();
        Token events = lexer.nextToken();
        Token state = lexer.nextToken();
        Token actions = lexer.nextToken();

        assertAll("Checking keywords",
                () -> assertEquals(TokenType.COMMANDS, commands.tokenType()),
                () -> assertEquals(TokenType.RESET_COMMANDS, resetCommands.tokenType()),
                () -> assertEquals(TokenType.EVENTS, events.tokenType()),
                () -> assertEquals(TokenType.STATE, state.tokenType()),
                () -> assertEquals(TokenType.ACTIONS, actions.tokenType())
        );
    }

    @Test
    void testLexingArrow() {
        Lexer lexer = createLexer("-->");
        Token arrow = lexer.nextToken();

        assertEquals(TokenType.ARROW, arrow.tokenType(), "Checking arrow token");
        assertEquals("-->", arrow.attribute(), "Checking arrow literal");
    }

    @Test
    void testLexingCharLiterals() {
        Lexer lexer = createLexer("'a'");
        Token charLiteral = lexer.nextToken();

        assertAll("Checking character literals",
                () -> assertEquals(TokenType.CHAR_LITERAL, charLiteral.tokenType()),
                () -> assertEquals("a", charLiteral.attribute())
        );
    }

    @Test
    void testLexingIdentifierWithDigits() {
        Lexer lexer = createLexer("state1 123variable");
        Token state1 = lexer.nextToken();
        Token var123 = lexer.nextToken();

        assertAll("Checking identifiers with digits",
                () -> assertEquals(TokenType.IDENTIFIER, state1.tokenType()),
                () -> assertEquals("state1", state1.attribute()),
                () -> assertEquals(TokenType.IDENTIFIER, var123.tokenType()),
                () -> assertEquals("123variable", var123.attribute())
        );
    }
}
