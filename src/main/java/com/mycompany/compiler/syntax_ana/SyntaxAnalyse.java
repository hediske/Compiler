package com.mycompany.compiler.syntax_ana;
public class SyntaxAnalyse {
    private  String[] gram = new String[]{
        "P → D ; I",
        "D → D ; D | id : T | id : F",
        "T → char | int | bool | string",
        "F → function ( P ) : T | function () : T",
        "I → I ; I | if ( E ) { I } | if ( E ) { I } ; else { I } | while ( E ) { I } | id = E | id = function (Par) { I } | id = function () { I }",
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
    SLR_parser sl = new SLR_parser(gram);

}

    





