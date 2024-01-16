package com.mycompany.compiler.syntax_ana;
public class SyntaxAnalyse {
    private  String[] gram = new String[]{
        "Pro → D ; I",
        "D → DL ; D",
        "DL → id : T | id : F",
        "T → char | int | bool | string",
        "F → function ( P ) : T | function ( ) : T",
        "I → IL ; I",
        "IL → if ( E ) { I } IFS | while ( E ) { I } | id = E | id = function ( Par ) { I } | id = function ( ) { I }",
        "IFS → else { I } | ɛ",
        "E → id ( P' ) | id ( ) | EL opari E | EL opbol E | EL oprel E | opuni E | ( E )",
        "EL → nb | id | str | litteral",
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
     ",", ";" , ":" , "char" , "bool", "string", "int" , "(" , ")" , "{", "}" , "if" , "else" , "while" , "function" , "=" , "nb" ,"id" , "str" , "litteral" , "opari" , "opbol" ,"oprel" , "opuni"
    };
    SLR_First_Follow sl = new SLR_First_Follow(gram,NonTerminal);
    public void ShowRules(){
        sl.ShowRules();
        sl.GetFirst();
        sl.ShowFirst();
        sl.GetFollow();
        sl.ShowFollow();
        SLR_Table_Parser par = new SLR_Table_Parser(gram, NonTerminal, sl.GetListFollow());
        System.out.print("The grammar given is");
        System.out.print(par.CheckIsSlr() ? " " : " not ");
        System.out.println("SLR");

    }
}




