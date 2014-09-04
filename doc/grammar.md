# Original Grammar

```antlr
S      : Assign '\n'
       | Query  '\n'
       ;
Assign : ID Eq Exp
       ;
Query  : ID QMARK
       ;
Exp    : Exp AND Exp
       | Exp  OR Exp
       | Exp XOR Exp
       |     NOT Exp
       | LP  Exp  RP
       | ID
       | TRUE
       | FALSE
       ;
```

# Broken Down Grammar

```antlr
S      : Assign '\n'
       | Query  '\n'
       ;
Assign : ID Eq Exp
       ;
Query  : ID QMARK
       ;
Exp    : Exp  AND Exp1 | Exp1
       ;
Exp1   : Exp1  OR Exp2 | Exp2
       ;
Exp2   | Exp2 XOR Exp3 | Exp3
       ;
Exp3   |     NOT Exp
       | LP  Exp  RP
       | ID
       | TRUE
       | FALSE
       ;
```
