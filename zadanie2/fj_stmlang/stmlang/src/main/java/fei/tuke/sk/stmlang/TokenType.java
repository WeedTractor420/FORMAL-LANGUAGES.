package fei.tuke.sk.stmlang;

public enum TokenType {
    // Keywords
    COMMANDS, EVENTS, RESET_COMMANDS, STATE, ACTIONS,

    // Symbols
    LBRACKET, // [
    RBRACKET, // ]
    LBRACE, // {
    RBRACE, // }
    ARROW,  // -->
    ASSIGN, // =

    // Literals
    IDENTIFIER, // variable names
    CHAR_LITERAL, // character literals, e.g., 'a'
    STRING_LITERAL, // string literals, e.g., "openDoor"

    // End of file
    EOF
}
