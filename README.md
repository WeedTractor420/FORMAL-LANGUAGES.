Gramatika v EBNF forme:

Calc -> Statement EOF;
Statement -> expr;
expr -> mul{("+" | "-") mul};
mul -> exp{("*" | "/") exp};
exp -> term["^" exp];
term -> NUMBER | "(" expr ")" | ("+" | "-") term;
NUMBER -> digit{digit};
digit -> "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"

Prilad odvodenia vyrazu (3*3) - (3*3):
    Calc => Statement EOF => expr EOF => mul - mul EOF => exp - exp EOF => term - term EOF => (expr) - (expr) EOF => (mul) - (mul) EOF => (exp * exp) - (exp * exp) EOF => (temr * term) - (term * term) EOF => (NUMBER * NUMBER) - (NUMBER * NUMBER) EOF => (3*3) - (3*3) EOF