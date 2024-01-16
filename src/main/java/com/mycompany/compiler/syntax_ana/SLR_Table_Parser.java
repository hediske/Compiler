package com.mycompany.compiler.syntax_ana;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


import java.util.Iterator;


public class SLR_Table_Parser{
    private String[ ] gram;
    private String[ ] NonTerminal;
    private ArrayList<String> AugGram;
    private HashMap<String,ArrayList<String>> Follows ;

    private ArrayList<Set<String>> Sets = new ArrayList<Set<String>>() ;
    private ArrayList<HashMap<String,Integer>> Action =new ArrayList<HashMap<String,Integer>>() ;
    private ArrayList<HashMap<String,Set<Integer>>> Shift =new ArrayList<HashMap<String,Set<Integer>>>() ;
    private ArrayList<String> ShiftProductions = new ArrayList<String>();



    
    public SLR_Table_Parser(String[] gram,String[] NonTerminal,HashMap<String,ArrayList<String>> Follows){    
        this.NonTerminal=NonTerminal;
        this.gram=gram;
        this.Follows=Follows;
        this.AugGram=generateAugmentedGrammar();
        generateSlrTable();
        showSets();
        showActions();
        showShifts();
        showShiftProductions();


    }

    

    private Boolean checkElemIsNonTerminal(String x){
        return Arrays.asList(NonTerminal).contains(x);
    }

    public void showSets(){
        System.out.println("------------------------------Sets----------------------------");
        for (int i = 0; i < Sets.size(); i++) {
            Set<String> currentSet = Sets.get(i);
            System.out.println("Set " + i + ": " + currentSet);
        }
    }


    public void showShifts(){
        System.out.println("------------------------------Shifts----------------------------");
        for (int i = 0; i < Shift.size(); i++) {
            HashMap<String,Set<Integer>> currentShift = Shift.get(i);
            System.out.println("Shift  " + i + ": " + currentShift.toString());
        }
    }

    public void showActions(){
        System.out.println("------------------------------Actions----------------------------");
        for (int i = 0; i < Action.size(); i++) {
            HashMap<String,Integer> currentAction = Action.get(i);
            System.out.println("Action  " + i + ": " + currentAction.toString());
        }
    }
    public void showShiftProductions(){
        System.out.println("------------------------------ShiftProductions----------------------------");
        for (int i = 0; i < ShiftProductions.size(); i++) {
            String currentProd = ShiftProductions.get(i);
            System.out.println("Production  " + i + ": " + currentProd);
        }
    }

    private String AdvancePointer(String s)
    {
        String el[] = s.split("•");
        if(el.length==1)
            return null;
        else
            {
                el[1]=el[1].substring(1);
                int x=el[1].indexOf(" ");

                if(x==-1){
                    el[1]=el[1]+" •";
                }
                else{

                    String s1 = el[1].substring(x);
                    String s2 =el[1].substring(0,x);

                    el[1]=s2+" •"+s1;
                }
                return(el[0]+el[1]);
            }
    }

    private String FindFollower(String s){
        int i=s.indexOf("•");
        if (i==-1 || i==s.length()-1){
            return null;
        }
        else 
                return s.substring(i).split(" ")[1];
    }

    private static String  addDot(String s){
       String elem[]=SLR_First_Follow.ExtractRuleElements(s);
        if(elem[1].equals("ɛ"))
            return elem[0]+" → •";
        else
        {
            if(elem[1].charAt(0)==' ')
                return elem[0]+" → •"+elem[1];
            else
                return elem[0]+" → • "+elem[1];
        }
            

    }

    public ArrayList<String> generateAugmentedGrammar(){
        ArrayList<String> res = new ArrayList<String>();
        String ProdctionAdded;
        String[] S= SLR_First_Follow.ExtractRuleElements(gram[0]);
        if (S.length>0){
             ProdctionAdded=S[0]+"' → "+S[0];
             res.add(ProdctionAdded);
        }
        for (String s:gram){
            String[] R= SLR_First_Follow.ExtractRuleElements(s);
            for (String rule : R[1].split(" \\| ")){
                res.add(R[0]+" → "+rule);
            }
        }
        return res;

    }

    private HashSet<String> closure(HashSet<String> elems){
        Boolean ok;
        do{
            ok=true;
            Iterator<String> it = elems.iterator();
            HashSet<String> S = new HashSet<String>();
            for(;it.hasNext();){
                String el = it.next();
                String Prod = SLR_First_Follow.ExtractRuleElements(el)[1];
                if(Prod.indexOf("•")!=-1 && Prod.indexOf("•")<Prod.length()-1)
                {
                    String next = Prod.substring(Prod.indexOf("•")+2).split("\\s+")[0];
                    if(!checkElemIsNonTerminal(next)  && !next.equals("ɛ")){
                        for(String s: AugGram){
                            if (SLR_First_Follow.ExtractRuleElements(s)[0].equals(next)){
                                String tr = addDot(s);
                                if(!elems.contains(tr) && !S.contains(tr))
                                  {
                                    ok=false;
                                    S.add(tr);
                                  }
                            }
                        }
                    }
                }
            }
            if(S.isEmpty())     
                ok=true;
            elems.addAll(S);
            S.clear();



        }while(!ok);
        // for (String s : elems)
        // {
        //     System.out.println(s);
        // }
        return elems;
        }


    private HashSet<String> goTO(Set<String> elems ,String X){
        HashSet<String> res = new HashSet<String>();
        Iterator <String> it = elems.iterator();
        for(;it.hasNext();)
        {
            String el = it.next();
            String next = FindFollower(el);
            if(next != null && next.equals(X)){
                res.add(AdvancePointer(el));
            }
        }
        return(closure(res));
    }
    private void AddShiftProductions(String str,int i){
        String first = SLR_First_Follow.ExtractRuleElements(str)[0];
        for (String s : Follows.get(first)){
            if(!ShiftProductions.contains(str))
                ShiftProductions.add(str);
            int j=ShiftProductions.indexOf(str);
            if(Shift.size()<=i){
                HashMap <String,Set<Integer>> hasm = new HashMap<String,Set<Integer>>();
                Set <Integer> st = new HashSet<Integer>();
                st.add(j);
                hasm.put(s, st);
                Shift.add(hasm);
            }
            else {
                if(!Shift.get(i).containsKey(s)){
                    Set <Integer> st = new HashSet<Integer>();
                    st.add(j);
                    Shift.get(i).put(s, st);
                }
                else{
                    Shift.get(i).get(s).add(j);
                }
            }
        }
    }
    private void generateShifts(){
        Shift.clear();

        for (int i=0;i<Sets.size();i++){
            Set<String> set = Sets.get(i);
            for  (String str : set){
                if (str.charAt(str.length()-1)=='•'){
                    if(str.substring(0,str.length()-2).equals(AugGram.get(0))){
                        ShiftProductions.add("ACC");
                        int j =ShiftProductions.indexOf("ACC");
                        if(Shift.size()<=i){
                            HashMap <String,Set<Integer>> hasm = new HashMap<String,Set<Integer>>();
                            Set <Integer> st = new HashSet<Integer>();
                            st.add(j);
                            hasm.put("$", st);
                            Shift.add(hasm);
                        }
                        else {
                            if(!Shift.get(i).containsKey("$")){
                                Set <Integer> st = new HashSet<Integer>();
                                st.add(j);
                                Shift.get(i).put("$", st);
                            }
                            else{
                                Shift.get(i).get("$").add(j);
                            }
                        }
                    }
                    else if (SLR_First_Follow.ExtractRuleElements(str)[1].equals("•")){
                        AddShiftProductions(str.replace("•", "ɛ"),i);
                    }
                    else{
                        AddShiftProductions(str.substring(0,str.length()-2),i);
                    }
                }
            }
            if(Shift.size()<=i){
                HashMap <String,Set<Integer>> hasm = new HashMap<String,Set<Integer>>();
                Shift.add(hasm);

            }


        }


    }
    private int getIndexList(Set<String> s)
    {
        return(Sets.indexOf(s));
    }
    
    private void generateSlrTable(){
        Action.clear();
        Set<String> I0 = closure((new HashSet<>(Collections.singletonList(addDot(AugGram.get(0))))));
        Sets.add(I0);
        int i = 0 ; 
        while(i<Sets.size()){
            Set <String > list = Sets.get(i);
            for (String s : list){
                if(FindFollower(s)!=null){
                    String X =FindFollower(s);
                    Set <String> list2 =goTO(list, X);

                int j = getIndexList(list2);
                if(j==-1)
                {
                    Sets.add(list2);
                    j=getIndexList(list2);
                }
                if (Action.size()<=i){
                    HashMap <String,Integer> hasm = new HashMap<String,Integer>();
                    hasm.put(X, j);
                    Action.add(hasm);
                }
                else{
                    if(!(Action.get(i).containsKey(X)))
                        Action.get(i).put(X,j);
                }

                }
            }
        if(Action.size()<=i)
            {
                HashMap <String,Integer> hasm = new HashMap<String,Integer>();
                Action.add(hasm);
            }
        i++;
        }

        generateShifts();

    }



    public boolean CheckIsSlr(){
        
        if(Sets.size()>0 && Action.size()>0 && Shift.size()>0){
                for (int i =0 ; i<Sets.size();i++){
                    HashMap<String, Set<Integer>> s1=Shift.get(i);
                    for (Set<Integer> k : s1.values())
                        if(k.size()>1)
                            return false;
                    HashMap<String, Integer> s2 = Action.get(i);
                    for (var x : s1.keySet()){
                        if (s2.keySet().contains(x))
                        {    
                            return false;
                        }
                    }
                }
        }
        else{
            return false;
        }
        return true;
        
    }










}
