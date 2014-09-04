# Original Grammar

```antlr
S      : Assign EOL
       | Query  EOL
       ;
Assign : ID   EQ Exp
       ;
Query  : ID  QMARK
       ;
Exp    : Exp AND Exp
       | Exp  OR Exp
       | Exp XOR Exp
       |     NOT Exp
       |  LP Exp  RP 
       | ID
       | TRUE
       | FALSE
       ;
```

# Basic Symbols

```antlr
ID     : ('a'..'z')
       ;
AND    : '&'
       ;
OR     : '|'
       ;
XOR    : '^'
       ;
NOT    : '~'
       ;
EQ     : '='
       ;
QMARK  : '?'
       ;
FALSE  : '0'
       ;
TRUE   : '1'
       ;
LP     : '('
       ;
RP     : ')'
       ;
EOL    : '\n'
       ;
NULL   : ''
       ;
```

# Broken Down Grammar

```antlr
S      : Assign EOL
       | Query  EOL
       ;
Assign : ID EQ Exp
       ;
Query  : ID QMARK
       ;
Exp    : Exp  AND Exp1 | Exp1
       ;
Exp1   : Exp1  OR Exp2 | Exp2
       ;
Exp2   : Exp2 XOR Exp3 | Exp3
       ;
Exp3   :     NOT Exp
       | LP  Exp  RP
       | ID
       | TRUE
       | FALSE
       ;
```

# Transformed Grammar

```antlr
S      : Assign EOL
       | Query  EOL
       ;
Assign : ID EQ Exp
       ;
Query  : ID QMARK
       ;
Exp    : Exp1 Exp_
       ;
Exp_   : AND Exp1 Exp_
       | NULL
       ;
Exp1   : Exp2 Exp1_
       ;
Exp1_  :  OR Exp2 Exp1_
       | NULL
       ;
Exp2   : Exp3 Exp2_
       ;
Exp2_  : XOR Exp3 Exp2_
       | NULL
       ;
Exp3   : NOT Exp
       | LP  Exp  RP
       | ID
       | TRUE
       | FALSE
       ;
```
