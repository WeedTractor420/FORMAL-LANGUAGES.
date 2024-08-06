ZADANIE 1:

Kalkulačka v Jave využíva gramatiku na rozpoznávanie a vyhodnocovanie aritmetických výrazov. Proces zahŕňa tvorbu lexera na tokenizáciu vstupného reťazca a parsera na syntaktickú analýzu tokenov a ich transformáciu do výrazu, ktorý môže byť vyhodnotený.

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


ZADANIE 2:

Program bude obsahovať lexer na tokenizáciu vstupného reťazca, parser na syntaktickú analýzu tokenov a generátor kódu na preklad do jazyka C. Gramatika v EBNF forme popisuje jednoduchý stavový automat s príkazmi, udalosťami, resetovacími príkazmi, iniciálnym stavom a stavmi.
Gramatika v EBNF forme ZADANIE 2:

1. StateMachine -> {Statement} EOF;
2. Statement -> Commands | Events | ResetCommands | InitState | State
3. Commands -> "commands" "{" { Command } "}";
4. Command -> Identifier "=" CHAR_LITERAL;
5. Events -> "events" "{" { Event } "}";
6. Event -> Identifier "=" CHAR_LITERAL;
7. ResetCommands -> "resetCommands" "{" { Identifier } "}";
8. State -> "state" Identifier StateContent;
9. StateContent -> "{" [Action] { Transition } "}";
10. Action -> "actions" "[" { Identifier } "]";
11. Transition -> Identifier "->" Identifier;
12. Identifier -> letter { letter | digit };
13. CHAR_LITERAL -> "'" character "'";
14. letter -> "a" | "b" | ... | "z" | "A" | ... | "Z";
16. digit -> "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9";
17. character -> /* all printable characters */;
18. EOF -> /* end of file token */;
