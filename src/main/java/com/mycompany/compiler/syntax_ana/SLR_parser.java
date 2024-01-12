package com.mycompany.compiler.syntax_ana;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SLR_parser{
    private String[] gram;
    private String[] NonTerminal;
    private HashMap<String,ArrayList<String>> first= new HashMap<String,ArrayList<String>>();
    public SLR_parser(String[] gram,String[] NonTerminal){
        this.gram= gram;
        this.NonTerminal = NonTerminal;
    }
 
    protected void ShowRules(){
        for (var x : gram )
        {
            System.out.println(x);
        }  
    }
  
    private String[] ExtractRuleElements(String x)
    {   
        String[] rule ;
            rule=x.split("→");
            rule[0]=rule[0].substring(0, rule[0].length()-1);//only conserve first 
            rule[1]=rule[1].substring(1);//only conserve second        
        return rule;

    }

    private Boolean checkElemIsNonTerminal(String x){
        return Arrays.asList(NonTerminal).contains(x);
    }

    public void GetFirst()
    {   String[] rule;
         String[] UniRule;
        String firstElem;
        for (var x : gram)
        {
            rule=ExtractRuleElements(x);
            UniRule = rule[1].split ("\\|");
            for (var y : UniRule)
            {
                if (y.charAt(0)==' ') 
                     y=y.substring(1);
                firstElem=y.split(" ")[0];

                if ((checkElemIsNonTerminal(firstElem)) || (firstElem.equals("ɛ")) ){
                    
                    if(!(first.containsKey(rule[0]))){
                        ArrayList <String> res = new ArrayList<>();
                        res.add(firstElem);
                        first.put(rule[0], res );
                    }
                    else if (!( first.get(rule[0]).contains(firstElem))){
                        first.get(rule[0]).add(firstElem);
                    }
                }
                else if(!(rule[0].equals(firstElem))) {

                    if(!(first.containsKey(rule[0]))){
                        ArrayList <String> res = new ArrayList<>();
                        res.add("First-"+firstElem);
                        first.put(rule[0], res );
                    }
                    else if (!( first.get(rule[0]).contains("First-"+firstElem))){
                        first.get(rule[0]).add("First-"+firstElem);
                    }




                }

                
            }
        }

        Boolean  allExtracted=true;
        do{
        for (String key : first.keySet()){
            for(String s : first.get(key)){
                if ( s.startsWith("First-")){
                    allExtracted=false;
                    String Terminal = s.substring(s.indexOf("-")+1);
                    for (String ch : first.get(Terminal))
                        {
                            if (!(first.get(key).contains(ch))){
                                first.get(key).add(ch);
                            }
                        }                   
                    }
                }
            }  
        } while (!allExtracted);

    first.forEach((key,value)->System.out.println("key : "+key+" value : "+value ));

    }

}