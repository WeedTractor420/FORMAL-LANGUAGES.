import fei.tuke.sk.Lexer;
import fei.tuke.sk.Parser;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @Test
    public void testParserAdditionWithNegativeResult() {
        Lexer lexer = new Lexer(new StringReader("234+1028"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(1262, result);
    }

    @Test
    public void testParserAdditionPositiveResult() {
        Lexer lexer = new Lexer(new StringReader("234+(-1028)"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(-794, result);
    }

    @Test
    public void testParserMultiplication() {
        Lexer lexer = new Lexer(new StringReader("10879*54"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(587466, result);
    }

    @Test
    public void testParserSubtractionWithNegativeResult() {
        Lexer lexer = new Lexer(new StringReader("286-1578"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(-1292, result);
    }

    @Test
    public void testParserSubtractionWithPositiveResult() {
        Lexer lexer = new Lexer(new StringReader("1000-320"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(680, result);
    }

    @Test
    public void testParserDivision() {
        Lexer lexer = new Lexer(new StringReader("144/12"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(12, result);
    }

    @Test
    public void testParserExp() {
        Lexer lexer = new Lexer(new StringReader("2^8"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(256, result);
    }

    @Test
    public void testParserMultiExp() {
        Lexer lexer = new Lexer(new StringReader("2^2^2"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(16, result);
    }

    @Test
    public void testParserCombined() {
        Lexer lexer = new Lexer(new StringReader("(3*2-5)^3-(5^2)"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(-24, result);
    }

    @Test
    public void testParserWithMultipleOperations() {
        Lexer lexer = new Lexer(new StringReader("10+5*2-3"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(17, result);
    }

    @Test
    public void testParserWithParentheses() {
        Lexer lexer = new Lexer(new StringReader("(10+5)*(3-1)"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(30, result);
    }

    @Test
    public void testParserWithComplexExpression() {
        Lexer lexer = new Lexer(new StringReader("2^3*(4-1)+10/2"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(29, result);
    }

    @Test
    public void testParserWithNestedParentheses() {
        Lexer lexer = new Lexer(new StringReader("((10+5)*2)-3"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(27, result);
    }

    @Test
    public void testParserWithNegativeExponent() {
        Lexer lexer = new Lexer(new StringReader("2^-3"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(0, result); // or any other predefined behavior for negative exponents
    }

    @Test
    public void testParserWithNegativeDivision() {
        Lexer lexer = new Lexer(new StringReader("6/-3"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(-2, result); // or any other predefined behavior for negative exponents
    }

    @Test
    public void testParserWithNegativeMultiplication() {
        Lexer lexer = new Lexer(new StringReader("2*-3"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(-6, result); // or any other predefined behavior for negative exponents
    }

    @Test
    public void testParserWithTwoNegativeMultiplication() {
        Lexer lexer = new Lexer(new StringReader("-2*-3"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(6, result); // or any other predefined behavior for negative exponents
    }

    @Test
    public void testParserWithTwoNegativeDivision() {
        Lexer lexer = new Lexer(new StringReader("-6/-3"));
        Parser parser = new Parser(lexer);
        int result = parser.statement();
        assertEquals(2, result); // or any other predefined behavior for negative exponents
    }

}
