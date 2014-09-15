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
Assign : EQ Exp
       ;
Query  : QMARK
       ;
E      : T  OR T
       | T
	   ;
T      : F XOR F
       | F
	   ;
F      : A AND A
       | A
	   ;
A      :   NOT A
       | G
	   ;
G      : LP EXP RP
       | ID
	   | LIT
	   ;
```


# Transformed Grammar

```antlr
S      : ID S_ EOL
       ;
S_     : Assign
       | Query
       ;
Assign : EQ Exp
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
F      : A F_
       ;
F_     : AND A F_
       | NULL
	   ;
A      : NOT A
       | G
	   ;
G      : LP EXP RP
       | ID
	   | LIT
	   ;
```
