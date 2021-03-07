# அகரம்

<p align="center">
  <img src="https://user-images.githubusercontent.com/18109258/110229583-9a5ee480-7f30-11eb-8ccd-1eb56e97da78.png" height="512px" width="512px"/>
</p>

<p align="center">
ஒரு தமிழ் நிரலாக்க மொழி
</p>


# பயன்பாடு

#### வாக்கியங்களை பதிப்பிக்க

`எழுது '123';`

#### எண்கணித செயல்பாடுகள்

`எழுது 1 + 2; // கூட்டல்`

`எழுது 1 - 2; // கழித்தல்`

`எழுது 1 * 2; // பெருக்கல்`

`எழுது 4/2; // வகுத்தல்`

`எழுது (1 * 2) + (4 / 2); //சிக்கலான எண்கணித செயல்பாடு`

#### Variable declaration

`மாறி எ = 10;`

#### If loop (ஒருவேளை)
<pre>
மாறி எ = 10;

  ஒருவேளை ( எ == 10) { 
  &nbsp;&nbsp;&nbsp;எழுது எ; 
  }
</pre>

#### While Loop (இருப்பின்)
<pre>
மாறி எ = 10;<br/>
இருப்பின்(எ > 0) { 
&nbsp;&nbsp;&nbsp;எழுது எ;
&nbsp;&nbsp;&nbsp;எ = எ - 1; 
}
</pre>


#### For loop (ஆக)
<pre>
ஆக ( மாறி எ = 1; எ < 5; எ = எ + 1) {
&nbsp;&nbsp;எழுது எ; 
}

</pre>


### Function declaration ( செயல்பாடு )
<pre>
செயல்பாடு  கூட்டு( அ, ஆ ) {  
	எழுது அ + ஆ; 
}
</pre>

#### Function call
<pre>
	கூட்டு(1,2);
</pre>

#### Return value from function (திருப்பு)
<pre>
செயல்பாடு  கூட்டு( அ, ஆ ) {  
	திருப்பு அ + ஆ; 
}
</pre>


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
