package com.mycompany.compiler.semantic_ana;

import java.util.Stack;

import com.mycompany.compiler.exception.SemanticException;
import com.mycompany.compiler.lexical_ana.lexic_unit;
import com.mycompany.compiler.lexical_ana.lexicalAnalyse;

public class SemanticAnalyse {

    //Stacks for Semantic values
    private static Stack<String> typeStack =new Stack<String>();

    //private logs
    private static StringBuilder typelogs = new StringBuilder();
    //syntax-directed definition
    public static void treatRule(String s , String rank , lexicalAnalyse ana)
    {
        treatType(s,rank,ana);

    }
    public static void treatRule(String s)
    {
        treatType(s);

    }

    private static <T> T readStack(Stack<T> Stack,String s){
        if(Stack.empty()){
            throw new SemanticException(s);
        }
        else{
            return Stack.pop();
        }
    }

    private static String readStackType(Stack<String> Stack){
        return readStack(Stack, "an ERROR occured in type checking ............");
    }

///simple manipulations
    private static void treatType (String s) throws SemanticException{
        switch (s){
            case  "Pro → D I","D → DL ; D","I → IL ; I" -> {
                String s1 = readStackType(typeStack);
                String s2 = readStackType(typeStack);
                if(s1.equals("EMPTY") && s2.equals("EMPTY"))
                    typeStack.push("EMPTY");
                else
                    typeStack.push("ERROR");
            }
            case "D → ɛ","I → ɛ","IFS → ɛ" -> typeStack.push("EMPTY");
            case "T → char" -> typeStack.push("CHAR");
            case "T → int" -> typeStack.push("INT");
            case "T → bool" -> typeStack.push("BOOL");
            case "T → string" -> typeStack.push("STRING");
            case "IL → if ( E ) { I } IFS" -> {
                String s1 = readStackType(typeStack);
                String s2 = readStackType(typeStack);
                String s3 = readStackType(typeStack);
                if(s1.equals("EMPTY") && s2.equals("EMPTY") && s3.equals("BOOL"))
                    typeStack.push("EMPTY");         
                else
                    typeStack.push("ERROR");
                
            }
            case "while ( E ) { I }" ->{
                String s1 = readStackType(typeStack);
                String s2 = readStackType(typeStack);
                if(s1.equals("EMPTY")&& s2.equals("BOOL")){
                    typeStack.push("EMPTY");
                }
                else 
                    typeStack.push("ERROR");
            }
            
            case "IFS → else { I }" -> {
                String s1 = readStackType(typeStack);
                if(s1.equals("EMPTY"))
                    typeStack.push("EMPTY");
                else    
                    typeStack.push("ERROR");
            }

            case "E → EL opari E" ->{
                String s1 = readStackType(typeStack);
                String s2 = readStackType(typeStack);
                if(s1.equals("INT")&&   s2.equals("INT")){
                    typeStack.push("INT");
                }
                else    
                    typeStack.push("ERROR");
            }

            case "E → EL opbol E" ->{
                String s1 = readStackType(typeStack);
                String s2 = readStackType(typeStack);
                if(s1.equals("BOOL")&& s2.equals("BOOL")){
                    typeStack.push("BOOL");
                }
                else    
                    typeStack.push("ERROR");
            }
            case "E → EL oprel E" ->{
                String s1 = readStackType(typeStack);
                String s2 = readStackType(typeStack);
                if(s1.equals(s2))
                {
                    if (s1.equals("INT")|| s1.equals("STR")){
                        typeStack.push("BOOL");
                    }
                    else 
                        typeStack.push("ERROR");
                }
                else{
                    typeStack.push("ERROR");
                }
            }
            case "E → opneg E" ->{
                String s1 = readStackType(typeStack);
                if(s1.equals("INT"))
                    typeStack.push(s1);
                else 
                    typeStack.push("ERROR");
            }
            case "E → opnot E" ->{
                String s1 = readStackType(typeStack);
                if(s1.equals("BOOL"))
                    typeStack.push(s1);
                else 
                    typeStack.push("ERROR");
            }
            case "E → ( E )", "E → EL","P → T","P' → E" -> {
                String s1 = readStackType(typeStack);
                typeStack.push(s1);
            }
            case "EL → nb" -> typeStack.push("INT");
            case "EL → str" -> typeStack.push("STR");
            case "EL → litteral" -> typeStack.push("CHAR");
            case "EL → true" -> typeStack.push("BOOL");
            case "EL → false" -> typeStack.push("BOOL");
            case "P' → E , P'" , "P → T , P" ->{
                String s1= readStackType(typeStack);
                String s2= readStackType(typeStack);
                if(s1.equals("ERROR")||s2.equals("ERROR")){
                    typeStack.push("ERROR");
                }
                else {
                    typeStack.push(s2+"-"+s1);
                }
            }
            case "F → function ( P ) : T" -> {
                String s1 = readStackType(typeStack);
                String s2 = readStackType(typeStack);
                if(s1.equals("ERROR")||s2.equals("ERROR")){
                    typeStack.push("ERROR");
                }
                else {
                    typeStack.push(s2+"->"+s1);
                }
            }
            


           
           
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        }
        
    }
    


//complex manipulations
    private static void treatType (String s , String rank , lexicalAnalyse ana) throws SemanticException{
        switch (s){
            case "EL → id"->{
                String key =ana.getKeyByRank(rank);
                if(key!=null){
                    lexic_unit lu = ana.GetLexem(key);
                    if(lu.getType().equals("NONE")){
                        typeStack.push("ERROR");
                    }
                    else{
                        typeStack.push(lu.getType());
                    }
                }
                else{
                    typeStack.push("ERROR");
                }
            }
            case "DL → T : id" , "DL → F : id" ->{
                String key = ana.getKeyByRank(rank);
                String type = readStackType(typeStack);
                if(key!=null){
                    lexic_unit lu = ana.GetLexem(key);
                    if(lu.getType().equals("NONE")){
                        ana.SetLexemType(type,key);
                        typeStack.push("EMPTY");
                    }
                    else{
                        typeStack.push("ERROR");
                        typelogs.append("The variable "+key+" is already defined !! ");
                        typelogs.append("\n");
                    }
                } 
                else {
                typeStack.push("ERROR");
                }



            }
            case "IL → id = E","Par → id : T" ->{
                String key = ana.getKeyByRank(rank);
                String type = readStackType(typeStack);
                System.err.println(type);

                if(key!=null){
                    lexic_unit lu = ana.GetLexem(key);
                    if(type.equals(lu.getType()))
                        typeStack.push("EMPTY");
                    else
                        {
                            typeStack.push("ERROR");
                            typelogs.append("ERROR , type not compatible ! expected "+lu.getType() + " and had "+type);
                            typelogs.append("\n");
                 }
                }
                else {
                    typeStack.push("ERROR");
                    typelogs.append("ERROR occured ! variable not found !");
                    typelogs.append("\n");
                }
            }
            
            case "id = function ( Par ) { I }", "Par → id : T , Par" ->{
                String key = ana.getKeyByRank(rank);
                String function = readStackType(typeStack);
                String type = readStackType(typeStack);
                if(function.equals("ERROR"))
                    typeStack.push("ERROR");
                else{
                    if(key!=null){
                        lexic_unit lu = ana.GetLexem(key);
                        if(type.equals(lu.getType().split("->")[0]))
                            typeStack.push("EMPTY");
                        else
                            {
                                typeStack.push("ERROR");
                                typelogs.append("ERROR , type not compatible ! expected "+lu.getType() + " and had "+type);
                                typelogs.append("\n");
                            }
                    }
                    else {
                        typeStack.push("ERROR");
                        typelogs.append("ERROR occured ! variable not found !");
                        typelogs.append("\n");
                    }




                }
            }
        
            case "id = function ( ) { I }" ->{
                String key = ana.getKeyByRank(rank);
                String function = readStackType(typeStack);
                if(function.equals("ERROR"))
                    typeStack.push("ERROR");
                else{
                    if(key!=null){
                        lexic_unit lu = ana.GetLexem(key);
                        if(lu.getType().indexOf("->")==0)
                            typeStack.push("EMPTY");
                        else
                            {
                                typeStack.push("ERROR");
                                typelogs.append("ERROR , type not compatible !");
                                typelogs.append("\n");
                           }
                    }
                    else {
                        typeStack.push("ERROR");
  
                    }




                    }



            }
        
        
        }
        
        
    }
          
    public  static void ShowStacks(){
        System.out.print("Types    :");
        System.out.println(typeStack.toString());
    }

    public static void ShowLogs(){
        if(typelogs.isEmpty())
            System.out.println("Your code is correct : No Type problems have been detected .");
        else
        {
            System.out.println("Your code is not correct : These Type problems have been detected :");
            System.out.println("Logs     :");
            System.out.println(typelogs.toString());
        }
    }
}




