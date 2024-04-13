import fei.tuke.sk.stmlang.Lexer;
import fei.tuke.sk.stmlang.Parser;
import fei.tuke.sk.stmlang.StateMachineDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    private Parser parser;

    void setUp(String input) {
        Lexer lexer = new Lexer(input); // Adjust this to how your Lexer is actually constructed
        parser = new Parser(lexer);
    }

    @Test
    void parseCommandsCorrectly() {
        setUp("commands { open 'o' }");
        StateMachineDefinition definition = parser.parse();
        assertEquals('o', definition.getCommands().get("open").charValue());
    }

    @Test
    void parseEventsCorrectly() {
        setUp("events { alarm = 'a' }");
        StateMachineDefinition definition = parser.parse();
        assertEquals('a', definition.getEvents().get("alarm").charValue());
    }

    @Test
    void parseResetCommandsCorrectly() {
        setUp("resetCommands { open }");
        StateMachineDefinition definition = parser.parse();
        assertTrue(definition.getResetCommands().contains("open"));
    }

    @Test
    void parseStateCorrectly() {
        setUp("state idle { actions [open] open --> active }");
        StateMachineDefinition definition = parser.parse();
        assertTrue(definition.getStates().containsKey("idle"));
    }

    @Test
    void parseMultipleStatesCorrectly() {
        setUp("state idle { } state active { }");
        StateMachineDefinition definition = parser.parse();
        assertTrue(definition.getStates().containsKey("idle") && definition.getStates().containsKey("active"));
    }

    @Test
    void parseWithMissingCommandsClosingBraceThrowsException() {
        setUp("commands { open 'o'");
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseWithMissingEventsClosingBraceThrowsException() {
        setUp("events { alarm = 'a'");
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseWithInvalidEventAssignmentThrowsException() {
        setUp("events { alarm > 'a' }");
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseWithIncorrectStateSyntaxThrowsException() {
        setUp("state { actions [open] }");
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseWithUnrecognizedTokenThrowsException() {
        setUp("unrecognized { }");
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseWithExtraBracesThrowsException() {
        setUp("commands { open 'o' }}");
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseWithInvalidTransitionSyntaxThrowsException() {
        setUp("state idle { open -> active }"); // Assuming '-->' is correct
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseWithMissingActionBracketThrowsException() {
        setUp("state idle { actions [open }");
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseStateWithNoActionsOrTransitions() {
        setUp("state emptyState {}");
        StateMachineDefinition definition = parser.parse();
        assertTrue(definition.getStates().containsKey("emptyState"));
        assertTrue(definition.getStates().get("emptyState").getActions().isEmpty());
        assertTrue(definition.getStates().get("emptyState").getTransitions().isEmpty());
    }

    @Test
    void parseMultipleCommandsAndEvents() {
        setUp("commands { open 'o' close 'c' } events { alarm = 'a' safe = 's' }");
        StateMachineDefinition definition = parser.parse();
        assertAll(
                () -> assertEquals('o', definition.getCommands().get("open").charValue()),
                () -> assertEquals('c', definition.getCommands().get("close").charValue()),
                () -> assertEquals('a', definition.getEvents().get("alarm").charValue()),
                () -> assertEquals('s', definition.getEvents().get("safe").charValue())
        );
    }

    @Test
    void parseCommandsAndResetCommands() {
        setUp("commands { open 'o' } resetCommands { open }");
        StateMachineDefinition definition = parser.parse();
        assertAll(
                () -> assertTrue(definition.getCommands().containsKey("open")),
                () -> assertTrue(definition.getResetCommands().contains("open"))
        );
    }

    @Test
    void parseIncompleteCommandsBlock() {
        setUp("commands { ");
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseEventsWithMissingAssignmentOperator() {
        setUp("events { alarm 'a' }"); // Missing '='
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseInvalidStateTransitionArrow() {
        setUp("state idle { open -!> active }"); // Invalid arrow syntax
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseStateWithUnmatchedBracketsInActions() {
        setUp("state confused { actions [open close }");
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseInvalidCharLiteralInEvents() {
        setUp("events { alarm = 123 }"); // Invalid char literal
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseMultipleStatesWithMissingBraces() {
        setUp("state firstState { actions [action1] action1 --> secondState state secondState actions [action2] }");
        assertThrows(RuntimeException.class, () -> parser.parse());
    }

    @Test
    void parseStateMachineDefinitionWithOnlyCommands() {
        setUp("commands { command1 'c' }");
        StateMachineDefinition definition = parser.parse();
        assertTrue(definition.getCommands().containsKey("command1"));
    }

    @Test
    void parseStateMachineDefinitionWithOnlyEvents() {
        setUp("events { event1 = 'U' }");
        StateMachineDefinition definition = parser.parse();
        assertTrue(definition.getEvents().containsKey("event1"));
    }

    @Test
    void parseStateMachineDefinitionWithOnlyResetCommands() {
        setUp("resetCommands { command1 }");
        StateMachineDefinition definition = parser.parse();
        assertTrue(definition.getResetCommands().contains("command1"));
    }

    @Test
    void parseStateMachineDefinitionWithOnlyStates() {
        setUp("state state1 { } state state2 { }");
        StateMachineDefinition definition = parser.parse();
        assertTrue(definition.getStates().containsKey("state1"));
        assertTrue(definition.getStates().containsKey("state2"));
    }

}
