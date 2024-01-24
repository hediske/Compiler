package com.mycompany.compiler.syntax_ana;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Set;
import java.util.Iterator;

import com.mycompany.compiler.exception.*;
import com.mycompany.compiler.lexical_ana.lexic_unit;
import com.mycompany.compiler.lexical_ana.lexicalAnalyse;;
public class SyntaxAnalyse {
    private  String[] gram = new String[]{
        "Pro → D I",
        "D → DL ; D | ɛ",
        "DL → T : id | F : id",
        "T → char | int | bool | string",
        "F → function ( P ) : T | function ( ) : T",
        "I → IL ; I | ɛ",
        "IL → if ( E ) { I } IFS | while ( E ) { I } | id = E | id = function ( Par ) { I } | id = function ( ) { I }",
        "IFS → else { I } | ɛ",
        "E → id ( P' ) | id ( ) | EL opari E | EL opbol E | EL oprel E | opuni E | ( E ) | EL",
        "EL → nb | id | str | litteral | true | false",
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
     ",", ";" , ":" , "true", "false", "char" , "bool", "string", "int" , "(" , ")" , "{", "}" , "if" , "else" , "while" , "function" , "=" , "nb" ,"id" , "str" , "litteral" , "opari" , "opbol" ,"oprel" , "opuni"
    };
    private SLR_First_Follow sl = new SLR_First_Follow(gram,NonTerminal);
    private SLR_Table_Parser par = null ;
    public SyntaxAnalyse(){
        sl.GetFirst();
        sl.ShowFirst();
        sl.GetFollow();
        sl.ShowFollow();
        SLR_Table_Parser par = new SLR_Table_Parser(gram, NonTerminal, sl.GetListFollow());
        this.par = par; 
        par.ShowActions();
        par.ShowSets();
        par.ShowShifts();
        par.ShowShiftProductions();
    }


    public void ShowRules(){
        sl.ShowRules();
    }

    public void ShowFirst(){
        sl.ShowFirst();

    }

    public void ShowFollow(){
        sl.ShowFollow();
    }

    public void ShowSets(){
        if(par!=null){
            par.ShowSets();
        }
    }
    public void ShowActions(){
        if(par!=null){
            par.ShowActions();
        }
    }
    public void ShowShifts(){
        if(par!=null){
            par.ShowShifts();
        }
    }

    public void ShowShiftProductions(){
        if(par!=null){
            par.ShowShiftProductions();
        }
    }
    public void CheckIsSlr(){
        if(par!=null){
            System.out.print("The grammar given is");
            System.out.print(par.CheckIsSlr() ? " " : " not ");
            System.out.println("SLR");
        }
    }

    private Queue<String> generateInput( ArrayList<lexic_unit>  input_lexic){
        Queue<String> input = new LinkedList<>();
            for (lexic_unit s: input_lexic){
                input.add(s.getUnilexid());
            }
            input.add("$");
            return input;
    }

    private void  showInput(Queue<String> input){
        System.out.print("Input    : ");
        java.util.Iterator <String> it = input.iterator();
        for(;it.hasNext();){
            String s = it.next();
            System.out.print(s+" ");
        }
        System.out.println();
    }
    private void  showPile(Stack <String> pile){
        System.out.print("Pile     : ");
        System.out.println(pile.toString());
    }
    

    public  void CodeSyntaxAnalyze(lexicalAnalyse ana) throws SyntaxicException , LexicalException{
        if(par!=null && ana!=null){
            Stack<String> pile = new Stack<String>();
            pile.add("0");
            Queue <String> input = generateInput(  ana.getUlarray());
            showInput(input);
            showPile(pile);
                            System.out.println();

            var Action = this.par.GetAction();
            var Shift = this.par.GetShift();
            var ShiftProductions = this.par.GetShiftProductions();


             while (!input.isEmpty()){
                String s = input.peek();
                String state = pile.peek();
                int indState = Integer.parseInt(state); 
                if( Action.get(indState).keySet().contains(s)){
                    pile.add(s);
                    pile.add(Action.get(indState).get(s).toString());
                    input.remove();
                }
                else if (Shift.get(indState).keySet().contains(s)){
                    Iterator <Integer> iterator = Shift.get(indState).get(s).iterator();
                    int rule ;
                    if(iterator.hasNext()){
                        rule = iterator.next();
                        if(iterator.hasNext())
                            throw new SyntaxicException("Error in your grammar ! it is not an SLR grammar");
                        String prod = ShiftProductions.get(rule);
                        if(prod.equals("ACC"))
                        {
                            System.out.println("ACCEPTED!");
                            break;
                        }


                            String [] rule_ex =SLR_First_Follow.ExtractRuleElements(prod);
                            int nbrReduce =rule_ex[1].split(" ").length;
                            if(rule_ex[1].equals("ɛ"))
                                nbrReduce=0;
                            for (int i=0;i<2*nbrReduce;i++){
                                pile.pop();
                            }
                            int x = Integer.parseInt(pile.peek());
                            pile.add(rule_ex[0]);
                            pile.add(Action.get(x).get(rule_ex[0]).toString());
                        
                    }


                }
                else {
                    throw new SyntaxicException("Sorry , There is an Error in your code syntaxe !");
                }
                showInput(input);
                showPile(pile);
                System.out.println();
                
            }

 


        }else if (ana == null){
            throw new LexicalException("Error in the lexical analyser !");

        }
        else{
            throw new SyntaxicException( " Error in the Grammar");
        }
    }
    

}


