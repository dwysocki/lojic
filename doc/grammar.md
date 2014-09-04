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
