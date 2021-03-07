# அகரம்

<p align="center">
  <img src="https://user-images.githubusercontent.com/18109258/110229583-9a5ee480-7f30-11eb-8ccd-1eb56e97da78.png" height="512px" width="512px"/>
</p>

<p align="center">
ஒரு தமிழ் நிரலாக்க மொழி
</p>


# பயன்பாடு

Print a string
`எழுது '123'; // print a letter`




# Grammar

## Statement
program        → declaration* EOF ;

declaration    → varDecl
               | funDecl
               | statement ;
               
funDecl        → "செயல்பாடு" function ;
function       → IDENTIFIER "(" parameters? ")" block ;
parameters     → IDENTIFIER ( "," IDENTIFIER )* ;

parameters     → IDENTIFIER ( "," IDENTIFIER )* ;
               
varDecl        → "மாறி" IDENTIFIER ( "=" expression )? ";" ;

statement      → exprStmt
               | printStmt
               | ifStmt
               | whileStmt
               | forStmt
               | returnStmt
               | block ;


ifStmt         → "ஒருவேளை" "(" expression ")" statement
               ( "இல்லையென்றால்" statement )? ;


returnStmt   -> "திருப்பு" expression ";"

whileStmt     -> "இருப்பின்" "(" expression ")" statement


forStmt        → "ஆக" "(" ( varDecl | exprStmt | ";" )
                 expression? ";"
                 expression? ")" statement ;


block          → "{" declaration* "}" ;

exprStmt       → expression ";" ;
printStmt      → "எழுது" expression ";" ;

## Expression

expression -> assignment;

assignment     → IDENTIFIER "=" assignment
                | logic_or ; ;


logic_or       → logic_and ( "or" logic_and )* ;
logic_and      → equality ( "and" equality )* ;

line -> expression

group -> "("  expression ")"

expression ->  binary | unary | literal | group

comparsion -> expression (">=" | "<=" | ">" | "<") expression

operator -> "+" | "-" | "*" | "/"

binary -> expression operator expression

unary -> operator expression | call

call  → primary ( "(" arguments? ")" )* ;

arguments      → expression ( "," expression )* ;

primary -> NUMBER | STRING | "உண்மை" | "பொய்" | IDENTIFIER;

grouping -> "(" expression ")" ;


##### we will support or and operation in symbol and text form
##### ||, அல்லது
##### &&, மற்றும்
