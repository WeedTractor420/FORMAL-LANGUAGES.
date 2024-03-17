Gramatika v EBNF forme:

1. Calc -> Statement EOF;
2. Statement -> expr;
3. expr -> mul{("+" | "-") mul};
4. mul -> exp{("*" | "/") exp};
5. exp -> term["^" exp];
6. term -> NUMBER | "(" expr ")" | ("+" | "-") term;
7. NUMBER -> digit{digit};
8. digit -> "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9";

Prilad odvodenia vyrazu (3*3) - (3*3):
    Calc => Statement EOF => expr EOF => mul - mul EOF => exp - exp EOF => term - term EOF => (expr) - (expr) EOF => (mul) - (mul) EOF => (exp * exp) - (exp * exp) EOF => (temr * term) - (term * term) EOF => (NUMBER * NUMBER) - (NUMBER * NUMBER) EOF => (3*3) - (3*3) EOF
