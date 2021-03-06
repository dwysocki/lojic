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
S      : ID S_ EOL
       ;
S_     : Assign
       | Query
       ;
Assign : EQ E
       ;
Query  : QMARK
       ;
E      : T  OR T
       | T
       ;
T      : F XOR F
       | F
       ;
F      : G AND G
       | G
       ;
G      :   NOT G
       | A
       ;
A      : LP EXP RP
       | ID
       | TRUE
       | FALSE
       ;
```


# Transformed Grammar

```antlr
S      : ID S_ EOL
       ;
S_     : Assign
       | Query
       ;
Assign : EQ E
       ;
Query  : QMARK
       ;
E      : T E_
       ;
E_     : OR T E_
       | NULL
       ;
T      : F T_
       ;
T_     : XOR F T_
       | NULL
       ;
F      : G F_
       ;
F_     : AND G F_
       | NULL
       ;
G      : NOT G
       | A
       ;
A      : LP EXP RP
       | ID
       | TRUE
       | FALSE
       ;
```

# First and Follow Sets

|  `X`     | `FIRST(X)`                        | `FOLLOW(X)`                  |
|----------|-----------------------------------|------------------------------|
| `S`      | `{ ID }`                          | `{ EOL, RP }`                |
| `S_`     | `{ EQ, QMARK }`                   | `{ EOL, RP }`                |
| `Assign` | `{ EQ }`                          | `{ EOL, RP }`                |
| `Query`  | `{ QMARK }`                       | `{ EOL }`                    |
| `E`      | `{ NOT, LP, ID, TRUE, FALSE }`    | `{ EOL, RP }`                |
| `E_`     | `{ NULL, OR }`                    | `{ EOL, RP }`                |
| `T`      | `{ NOT, LP, ID, TRUE, FALSE }`    | `{ EOL, OR, RP }`            |
| `T_`     | `{ NULL, XOR }`                   | `{ EOL, OR, RP }`            |
| `F`      | `{ NOT, LP, ID, TRUE, FALSE }`    | `{ EOL, XOR, RP }`           |
| `F_`     | `{ NULL, AND }`                   | `{ EOL, XOR, RP }`           |
| `G`      | `{ NOT }`                         | `{ EOL, AND, RP }`           |
| `A`      | `{ LP, ID, TRUE, FALSE }`         | `{ EOL, OR, XOR, AND, RP }`  |
