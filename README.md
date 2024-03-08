# Compiler

Hello , This project is a compiler based on three analysers :
	<ul>
			<li> <b>lexical analyser</b>
					&nbsp; This analyser provides for the user the possiblilty of using many types and symbols
			<li> <b>Syntax analyser</b>
					&nbsp; This analyser provides for the user the possiblilty of generating First,Follows and Slr table based on the grammar given
			<li><b>Semantic analyser</b>
					&nbsp; This analyser provides for the user the possiblilty of testing if the grammar is correct semantically by verifying the Types .
				
<h1>
How to Run </h1>

To run this app you need to install Java Jdk 17 or above :
		You can check the version by typing this command `java --version`
	<br>
		 First , change directory to compiler folder :
				
	   cd compiler
	
	
Then run this command
			
	  mvn exec:java -Dexec.mainClass="com.mycompany.compiler.main.Compiler"
<h1>Lexical Analyser</h1>
<h4>Symbols table</h4>

| Symbol| Types |
| ----------- | ----------- |
| if| NONE|
| else| NONE|
| true| BOOL|
| false| BOOL|
| function| NONE|
| while| NONE|							
| for| NONE|
| string| NONE|			
| bool| NONE|
| int| NONE|
| char| NONE|	

<h4>Language </h4>

<p align=center>
Delimiter  →  space|tab|lineBreak  <br>
Num  →  (chiffre)<sup>+</sup> <br>
id  →  lettre(chiffre  +  lettre)<sup>+</sup> <br>
opari  →  +| − | ∗ | /  <br>
oprel  →==  |  <  |  <=  |  <>  |  >  |  >=  <br>
opnot  →!  <br>
opneg→  –  <br>
str  →  ”lettre<sup>*</sup>”  <br>
opbol→ || |  &&  <br>
while→  while  <br>
if→  if   <br>
else→  else   <br>
char→  char   <br>
string→  string   <br>
bool→  bool   <br>
function→  f unction  <br> 
true→  true   <br>
false→  false   <br>
=  →=   <br>
:  →:   <br>
;  →;   <br>
)  →)   <br>
(→  (   <br>
} →}   <br>
{ → { <br>
</p>

<h1> Syntax Analyser </h1>
This package can generate for the user 
	<li> 
		First
		</li>
			<li> 
		Follows
		</li>	<li> 
		SLR table
		</li>
	and can verfiy if the grammar is SLR or not
	
To add a grammar to your Compiler you can modify file `grammar.json` located in **src/ressources**

<h4>Example</h4>

    <small>
    {
    "Grammar":  [<br>
    "Pro → D I",<br>
    "D → DL ; D | ɛ",<br>
    "DL → T : id | F : id",
    <br>
    "T → char | int | bool | string",
    <br>
    "F → function ( P ) : T | function ( ) : T",
    <br>
    "I → IL ; I | ɛ",
    <br>
    "IL → if ( E ) { I } IFS | while ( E ) { I } | id = E | id = function ( Par ) { I } | id = function ( ) { I }",
    <br>
    "IFS → else { I } | ɛ",
    <br>
    "E → id ( P' ) | id ( ) | EL opari E | EL opbol E | EL oprel E | opneg E | opnot E | ( E ) | EL",
    <br>
    "EL → nb | id | str | litteral | true | false",
    <br>
    "P → T | T , P",
    <br>
    "P' → E | E , P'",
    <br>
    "Par → id : T | id : T , Par"
    <br>
    ]
    <br>
     }</small>


The grammar must respect these rules : 	<ol>
		<li>You need To put a Space betwwen each element of the production
		<li>You use this arrow for the rule definition `→`
		<li>this symbol for the epsilon `ɛ`
		<li>Uppercase for the Non-Terminal
		</ol>

<h4>Example</h4>

> E' → E<br>
>E → E + T<br>
>E → T<br>
>T → T * F<br>
>T → F<br>
>F → ( E )<br>
>F → id<br>


To add a code for testing the Syntax analyser ! your file must be also located under **src/ressources**

<h1> Semantic analyser </h1>
	The goal of this analyser  is to check the type in the code to compile ! It checks if the variables were declared , if statements are correct and if expressions have correct types .

			
	
