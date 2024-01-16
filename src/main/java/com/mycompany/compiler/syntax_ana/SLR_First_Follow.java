package com.mycompany.compiler.syntax_ana;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SLR_First_Follow{
    private String[] gram;
    private String[] NonTerminal;
    private HashMap<String,ArrayList<String>> first= new HashMap<String,ArrayList<String>>();
    private HashMap<String,ArrayList<String>> follow= new HashMap<String,ArrayList<String>>();
    public SLR_First_Follow(String[] gram,String[] NonTerminal){
        this.gram= gram;
        this.NonTerminal = NonTerminal;
    }
 
    protected void ShowRules(){
        for (var x : gram )
        {
            System.out.println(x);
        }  
    }
  
    protected Boolean checkGenerateEpsilon(String res ){
        boolean x = false;
        for (String s:this.gram){
            String[] l = ExtractRuleElements(s);
            if((l[0].equals(res)) && (l[1].indexOf('ɛ') !=-1))
            {
                x=true;
                break;
            }
        }
        return x;
    }

    protected static String[] ExtractRuleElements(String x)
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

    public void 
    
    GetFirst()
    {   
        first.clear();
        String[] rule;
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

                    boolean ok = false;
                    do {
                        ok=true;
                    if(checkGenerateEpsilon(firstElem) && y.split(" ").length>1){
                        String follower = y.split(" ")[1];
                        if (!(follower.isEmpty()) && !(follower==null)){
                            if(checkElemIsNonTerminal(follower) || follower.equals("ɛ")){
                                first.get(rule[0]).add(follower);
                            }
                            else{
                                ok=false;
                                if(!(rule[0].equals(firstElem))){
                                    first.get(rule[0]).add("First-"+follower);
                                }
                                firstElem=follower;
                            }

                        }
                    } 
                    else if (checkGenerateEpsilon(firstElem) && y.split(" ").length==1){
                        first.get(rule[0]).add("ɛ");
                    }
                    }while(!ok);


                }

                
            }
        }

        Boolean  allExtracted=true;
        do{
        allExtracted=true;
        Iterator<Map.Entry<String, ArrayList<String>>> iteratorMap = first.entrySet().iterator();
            for (;iteratorMap.hasNext();){
                ArrayList<String> AddedList=new ArrayList<>();
                Map.Entry<String, ArrayList<String>> entry = iteratorMap.next();
                String key = entry.getKey();
                ArrayList<String> value =entry.getValue();
                Iterator<String> iterator = value.iterator();

                while(iterator.hasNext()){
                    String el = iterator.next();

                    if ( el.startsWith("First-")){
                      allExtracted=false;

                        String Terminal = el.substring(el.indexOf("-")+1);
                        iterator.remove();
                        Iterator<String> it2 = first.get(Terminal).iterator();
                        while (it2.hasNext()) {
                            String ch = it2.next();
                            if (!first.get(key).contains(ch) && !ch.equals("ɛ")) {
                                AddedList.add(ch);
                            }
                        }
                    }
                }  
                value.addAll(AddedList );
            }
        }  while (!allExtracted);

    }

    public void ShowFirst(){

        System.out.println("------------------------- ------------------------- SHOWING GRAMMAR FIRST ------------------------- -------------------------");
        first.forEach((key,value)->System.out.println("key : "+key+" value : "+value ));
        System.out.println("------------------------- ------------------------- ------------------------- ------------------------- -------------------------");
    
    }
    public HashMap<String,ArrayList<String>> GetListFirst(){
        return first;
    }

    public void GetFollow(){  
        follow.clear();
        boolean flag_first_rule = true;
         String[] rule;
         String[] UniRule;  
        for (String s : gram)
        {   
            rule=ExtractRuleElements(s);
            if(flag_first_rule){
                flag_first_rule=false;
                ArrayList<String> res = new ArrayList<>();
                res.add("$"); 
                follow.put(rule[0], res );
            }
            UniRule=rule[1].split("\\|");
            for (String ch: UniRule){
                String[] Param ;
                Param=ch.split("\\ ");
                for (int i=0;i<Param.length;i++){
                    String p = Param[i];
                    if  (!checkElemIsNonTerminal(p) && !p.equals("ɛ") && !p.isEmpty() && !(p==null))
                    {
                        if(i==Param.length-1)//lastelement
                            {
                                if( !(rule[0].equals(p)))
                                {
                                    
                                    if(!(follow.containsKey(p))){
                                        ArrayList <String> res = new ArrayList<>();
                                        res.add("Follow-"+rule[0]);
                                        follow.put(p, res );
                                    }
                                    else if (!( follow.get(p).contains("Follow-"+rule[0])) ){
                                        follow.get(p).add("Follow-"+rule[0]);
                                    }

                                }
                            }
                        else 
                            {
                                if(checkElemIsNonTerminal(Param[i+1]))
                                {
                                    if(!(follow.containsKey(p))){
                                        ArrayList <String> res = new ArrayList<>();
                                        res.add(Param[i+1]);
                                        follow.put(p, res );
                                    }
                                    else if (!( follow.get(p).contains(Param[i+1])) ){
                                        follow.get(p).add(Param[i+1]);
                                    }
                                }
                                else if  (!(Param[i+1].equals("ɛ"))) //terminal next to another terminal (not in our case)
                                {
                                    boolean ok =true;
                                    String next=Param[i+1];
                                    int index = i+1;
                                    while(ok)
                                    {
                                        
                                        if(!(follow.containsKey(p))){
                                            ArrayList <String> res = new ArrayList<>();
                                            res.add("First-"+next);
                                            follow.put(p, res );
                                            }
                                        else if (!( follow.get(p).contains("First-"+next) )){
                                            follow.get(p).add("First-"+next);
                                        }

                                        if(checkGenerateEpsilon(next))
                                            {
                                                index=index+1;
                                                if(index==Param.length)
                                                    {
                                                        if (!( follow.get(p).contains("Follow-"+rule[0])) )
                                                        {
                                                        follow.get(p).add("Follow-"+rule[0]);
                                                        }
                                                        ok=false;
                                                    }
                                                else 
                                                    {
                                                    next = Param[index];
                                                    if(checkElemIsNonTerminal(next))
                                                        {
                                                            if (!( follow.get(p).contains(next) ))
                                                            {
                                                            follow.get(p).add(next);
                                                            }
                                                            ok=false;
                                                        }

                                                }

                                            }   
                                            else {
                                                ok = false;
                                            }
                                    }
                                }
                            
                            
                            }
                        

                    }

                }
            }
        }


        Boolean  allExtracted=true;
        do{
        allExtracted=true;
        Iterator<Map.Entry<String, ArrayList<String>>> iteratorMap = follow.entrySet().iterator();
            for (;iteratorMap.hasNext();){
                ArrayList<String> AddedList=new ArrayList<>();
                Map.Entry<String, ArrayList<String>> entry = iteratorMap.next();
                String key = entry.getKey();
                ArrayList<String> value =entry.getValue();
                Iterator<String> iterator = value.iterator();

                while(iterator.hasNext()){
                    String el = iterator.next();

                    if ( el.startsWith("Follow-")){
                        allExtracted=false;

                        String Terminal = el.substring(el.indexOf("-")+1);
                        iterator.remove();

                        Iterator<String> it2 = follow.get(Terminal).iterator();
                        while (it2.hasNext()) {
                            String ch = it2.next();
                            if (!follow.get(key).contains(ch) ) {
                                AddedList.add(ch);
                            }
                        }
                    }


                }  
                value.addAll(AddedList );
            }
        }  while (!allExtracted);


        do{
        allExtracted=true;
        Iterator<Map.Entry<String, ArrayList<String>>> iteratorMap = follow.entrySet().iterator();
            for (;iteratorMap.hasNext();){
                Map.Entry<String, ArrayList<String>> entry = iteratorMap.next();
                ArrayList<String> AddedList2=new ArrayList<>();
                String key = entry.getKey();
                ArrayList<String> value =entry.getValue();
                Iterator<String> iterator = value.iterator();

                while(iterator.hasNext()){
                    String el = iterator.next();

                    if ( el.startsWith("First-")){
                      allExtracted=false;

                        String Terminal = el.substring(el.indexOf("-")+1);
                        iterator.remove();
                        if(first.containsKey(Terminal)){
                        Iterator<String> it2 = first.get(Terminal).iterator();
                        while (it2.hasNext()) {
                            String ch = it2.next();
                            if (!follow.get(key).contains(ch) && !ch.equals("ɛ")) {
                                AddedList2.add(ch);
                            }
                        }
                        }
                    }
                }  
                value.addAll(AddedList2 );
            }
        }  while (!allExtracted);

    }

        
        



public void ShowFollow(){

    System.out.println("------------------------- ------------------------- SHOWING GRAMMAR FOLLOWS ------------------------- -------------------------");
    follow.forEach((key,value)->System.out.println("key : "+key+" value : "+value ));
    System.out.println("------------------------- ------------------------- ------------------------- ------------------------- -------------------------");

}
public HashMap<String,ArrayList<String>> GetListFollow(){
    return follow;
}


}