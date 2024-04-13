Gramatika v EBNF forme ZADANIE 1:

1. Calc -> Statement EOF;
2. Statement -> expr;
3. expr -> mul{("+" | "-") mul};
4. mul -> exp{("*" | "/") exp};
5. exp -> term["^" exp];
6. term -> NUMBER | "(" expr ")" | ("+" | "-") term;
7. NUMBER -> digit{digit};
8. digit -> "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9";

Prilad odvodenia vyrazu (3 * 3) - (3 * 3):
    Calc => Statement EOF => expr EOF => mul - mul EOF => exp - exp EOF => term - term EOF => (expr) - (expr) EOF => (mul) - (mul) EOF => (exp * exp) - (exp * exp) EOF => (temr * term) - (term * term) EOF => (NUMBER * NUMBER) - (NUMBER * NUMBER) EOF => (3 * 3) - (3 * 3) EOF

Gramatika v EBNF forme ZADANIE 2:

1. StateMachine -> (Statement) EOF;
2. Statement -> Commands | Events | ResetCommands | InitState | State
3. Commands -> "commands" "{" { Command } "}";
4. Command -> Identifier "=" CHAR_LITERAL;
5. Events -> "events" "{" { Event } "}";
6. Event -> Identifier "=" CHAR_LITERAL;
7. ResetCommands -> "resetCommands" "{" { Identifier } "}";
8. State -> "state" Identifier StateContent;
9. InitState -> "state" "initState" StateContent;
10. StateContent -> "{" [Action] { Transition } "}";
11. Action -> "actions" "[" { Identifier } "]";
12. Transition -> Identifier "->" Identifier;
13. Identifier -> letter { letter | digit };
14. CHAR_LITERAL -> "'" character "'";
15. letter -> "a" | "b" | ... | "z" | "A" | ... | "Z";
16. digit -> "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9";
17. character -> /* all printable characters */;
18. EOF -> /* end of file token */;


