package com.mycompany.compiler.syntax_ana;
public class SyntaxAnalyse {
    private  String[] gram = new String[]{
        "Pro → D ; I",
        "D → D ; D | id : T | id : F",
        "T → char | int | bool | string",
        "F → function ( P ) : T | function ( ) : T",
        "I → I ; I | if ( E ) { I } | if ( E ) { I } ; else { I } | while ( E ) { I } | id = E | id = function ( Par ) { I } | id = function ( ) { I }",
        "E → nb | id | str | litteral | E ( P' ) | E ( ) | E opari E | E opbol E | E oprel E | opuni E",
        // "str → \" C \"",
        // "C → litteral C | ε",
        "P → T | T , P",
        "P' → E | E , P'",
        "Par → id : T | id : T , Par",
        // "oprel → < | > | <= | >= | <> | ==",
        // "opbol → || | &&",
        // "opari → + | - | / | *",
        // "opuni → ! | -"

    };

    private String[] NonTerminal = new String[]{
      ";" , ":" , "char" , "bool", "string", "int" , "(" , ")" , "{", "}" , "if" , " else " , "while" , "function" , "=" , "nb" ,"id" , "str" , "litteral" , "opari" , "opbol" ,"oprel" , "opuni"
    };
    SLR_parser sl = new SLR_parser(gram,NonTerminal);
    public void ShowRules(){
        sl.ShowRules();
        sl.GetFirst();
    }
}

    





