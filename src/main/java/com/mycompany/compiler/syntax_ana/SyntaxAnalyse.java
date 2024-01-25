package com.mycompany.compiler.syntax_ana;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mycompany.compiler.exception.*;
import com.mycompany.compiler.lexical_ana.lexic_unit;
import com.mycompany.compiler.lexical_ana.lexicalAnalyse;
import com.mycompany.compiler.semantic_ana.SemanticAnalyse;
public class SyntaxAnalyse {
    private  String[] gram = new String[]{};

     private String[] NonTerminal = new String[]{};

    private void loadGrammar() throws IOException , JSONException{
        String JsonFilePath = "src\\main\\ressources\\grammar.json";
        String JsonString;
        ArrayList<String> gram = new ArrayList<String>();
        ArrayList<String> NonTerminal = new ArrayList<String>();
        try{
            JsonString = new String(Files.readAllBytes(Paths.get(JsonFilePath)));
            JSONObject Grammar = new JSONObject(JsonString);
            JSONArray GramElemnts =Grammar.getJSONArray("Grammar");
            for (int i =0;i<GramElemnts.length();i++){
                gram.add(GramElemnts.getString(i));
            }
            JSONArray nonterminal =Grammar.getJSONArray("NonTerminals");
            for (int i =0;i<nonterminal.length();i++){
                NonTerminal.add(nonterminal.getString(i));
            }
            this.gram=gram.toArray(new String[0]);
            this.NonTerminal=NonTerminal.toArray(new String[0]);
        }
        catch(IOException e){
            System.out.println("Error , Missing grammar JSON file !!!");
        }
        catch(JSONException e){
            System.out.println("Error in your grammar details and informations !!");
        }

    }

    private SLR_First_Follow sl ;
    private SLR_Table_Parser par = null ;
    public SyntaxAnalyse() throws IOException , JSONException{
        loadGrammar();
        System.out.println("---------------------GRAMMAR LOADED SUCCESSFULLY---------------------");
        sl = new SLR_First_Follow(gram,NonTerminal);
        for (String s : this.gram)
        System.out.println(s);
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
        CheckIsSlr();
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
        Queue<String> input = input_lexic.stream()
                .map(s->{
                    if(s.getUnilexid().equals("id"))
                        return s.getUnilexid()+"-"+s.getRangerid();
                    else {
                        return s.getUnilexid();
                    }
                    
                    
                } 
                )
                .collect(Collectors.toCollection(LinkedList::new));
            input.add("$");
            return input;
    }

    private void  showInput(Queue<String> input){
        System.out.print("Input    : ");
        input.forEach((x) -> System.out.print(x+" "));
        System.out.println();
    }
    private void  showPile(Stack <String> pile){
        System.out.print("Pile     : ");
        System.out.println(pile.toString());
    }
    private String retrieveRangerId (String s_old) throws SyntaxicException{
        if(s_old.startsWith("id-")){
            return s_old.substring("id-".length());
        }
        else 
        {     
           throw new SyntaxicException("Encountred an error in your code ");
        }    
    }

    public  void CodeSyntaxAnalyze(lexicalAnalyse ana) throws SyntaxicException , LexicalException{
        if(par!=null && ana!=null){
            Stack<String> pile = new Stack<String>();
            pile.add("0");
            Queue <String> input = generateInput(ana.getUlarray());
            System.out.println("--------------------------------- Start Syntax Verification ----------------------------------");
            showInput(input);
            showPile(pile);
            var Action = this.par.GetAction();
            var Shift = this.par.GetShift();
            var ShiftProductions = this.par.GetShiftProductions();

            String id="";
            while (!input.isEmpty()){
                String s = input.peek();
                if(s.startsWith("id-")){
                    id=retrieveRangerId(s);
                    System.out.println("id nouveau :"+id);
                    s="id";
                }
                String state = pile.peek();
                int indState = Integer.parseInt(state); 
                if( Action.get(indState).keySet().contains(s)){
                    pile.add(s);
                    pile.add(Action.get(indState).get(s).toString());
                    input.remove();
                    System.out.println("Action   : SHIFT TO "+Action.get(indState).get(s));
                    System.out.println();                    
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
                            System.out.println("Action   : ACCEPTED!");
                            System.out.println();
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
                            if(!prod.contains("id"))
                                SemanticAnalyse.treatRule(prod);    
                            else
                            {
                              SemanticAnalyse.treatRule(prod,id,ana);
                            }
                            System.out.println("Action   : REDUCE BY "+prod);
                            System.out.println();
                        
                    }


                }
                else {
                    throw new SyntaxicException("Sorry , There is an Error in your code syntaxe !");
                }
                showInput(input);
                showPile(pile);
                SemanticAnalyse.ShowStacks();
            }
            SemanticAnalyse.ShowLogs();
 


        }else if (ana == null){
            throw new LexicalException("Error in the lexical analyser !");

        }
        else{
            throw new SyntaxicException( " Error in the Grammar");
        }
    }
    

}


