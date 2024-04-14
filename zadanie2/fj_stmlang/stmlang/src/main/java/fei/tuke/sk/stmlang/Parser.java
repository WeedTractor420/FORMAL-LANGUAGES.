package fei.tuke.sk.stmlang;

import java.util.*;

public class Parser {
    private final Lexer lexer;
    private Token symbol;
    private StateMachineDefinition definition;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.symbol = lexer.nextToken(); // Initialize the first token
    }

    public StateMachineDefinition parse() {
        definition = new StateMachineDefinition();
        Set<TokenType> first = Set.of(
                TokenType.COMMANDS,
                TokenType.EVENTS,
                TokenType.RESET_COMMANDS,
                TokenType.STATE);
        while (first.contains(symbol.tokenType())) {
            switch (symbol.tokenType()) {
                case COMMANDS -> commands();
                case EVENTS -> events();
                case RESET_COMMANDS -> resetCommands();
                case STATE -> state();
                default -> consume(); // Consume token if it does not match expected ones to prevent infinite loop
            }
        }
        match(TokenType.EOF);
        return definition;
    }

    private void commands() {
        match(TokenType.COMMANDS);
        match(TokenType.LBRACE);
        while (symbol.tokenType() != TokenType.RBRACE) {
            String commandName = symbol.attribute();
            match(TokenType.IDENTIFIER); // Move past the command name
            if (symbol.tokenType() == TokenType.CHAR_LITERAL) { // Ensure the next token is a CHAR_LITERAL
                char commandValue = symbol.attribute().charAt(0);
                consume(); // Move past the command value
                definition.addCommand(commandName, commandValue);
            }else{
                throw new StateMachineException("Expected toke: CHAR_LITERAL but found :" + symbol.tokenType());
            }
        }
        match(TokenType.RBRACE);
    }

    private void events() {
        match(TokenType.EVENTS);
        match(TokenType.LBRACE);
        while (symbol.tokenType() != TokenType.RBRACE) {
            String eventName = symbol.attribute();
            match(TokenType.IDENTIFIER);
            match(TokenType.ASSIGN); // Ensure we correctly match the assignment symbol
            if (symbol.tokenType() == TokenType.CHAR_LITERAL) { // Ensure the next token is a CHAR_LITERAL
                char eventValue = symbol.attribute().charAt(0);
                consume(); // Move past the event value
                definition.addEvent(eventName, eventValue);
            }else{
                throw new StateMachineException("Expected token : CHAR_LITERAL but found: " + symbol.tokenType());
            }
        }
        match(TokenType.RBRACE);
    }

    private void resetCommands() {
        match(TokenType.RESET_COMMANDS);
        match(TokenType.LBRACE);
        Set<String> resetCommands = new HashSet<>();
        while (symbol.tokenType() == TokenType.IDENTIFIER) {
            String commandName = symbol.attribute();
            consume(); // Move past the reset command
            resetCommands.add(commandName);
        }
        match(TokenType.RBRACE);
        resetCommands.forEach(definition::addResetCommands);
    }

    private void state() {
        match(TokenType.STATE);
        String stateName = symbol.attribute();
        //checking for initState
        if(definition.getInitialStateName() == null){
            definition.setInitialStateName(stateName);
        }
        consume(); // Move past the state name

        StateDefinition stateDefinition = new StateDefinition();

        // Check if there are actions
        if (symbol.tokenType() == TokenType.LBRACE) {
            consume();
            // If ACTIONS keyword is present
            if (symbol.tokenType() == TokenType.ACTIONS) {
                consume(); // Move past ACTIONS keyword
                stateDefinition.getActions().addAll(actions()); // Collect and add actions
            }
        }

        // Process transitions, which can directly follow actions or the state name
        while (symbol.tokenType() == TokenType.IDENTIFIER) { // Transition starts with an IDENTIFIER
            stateDefinition.addTransition(transition()); // Collect and add a transition
        }

        definition.addState(stateName, stateDefinition);
        match(TokenType.RBRACE);
    }


    private List<String> actions() {
        match(TokenType.LBRACKET);
        List<String> actions = new ArrayList<>();
        // Collect actions until we reach a closing brace
        while (symbol.tokenType() != TokenType.RBRACKET) {
            actions.add(symbol.attribute());
            match(TokenType.IDENTIFIER); // Move past the action
        }
        match(TokenType.RBRACKET); // Move past the closing brace for actions
        return actions;
    }

    private TransitionDefinition transition() {
        String commandName = symbol.attribute(); // This is the command name triggering the transition
        consume(); // Move past the command name

        match(TokenType.ARROW); // Assuming ARROW represents the transition arrow

        String targetState = symbol.attribute(); // This is the target state of the transition
        consume(); // Move past the target state name

        return new TransitionDefinition(commandName, targetState);
    }

    private void match(TokenType expected) {
        if (symbol.tokenType() == expected) {
            consume();
        }else {
            throw new StateMachineException("Expected token: " + expected + ", but found: " + symbol.tokenType());
        }
    }

    private void consume() {
        if (symbol != null) {
            symbol = lexer.nextToken();
        }else{
            throw new StateMachineException("Attempted to consume beyond the end of input.");
        }
    }
}
