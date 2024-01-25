package com.mycompany.semantic_ana;

import java.util.Stack;

public class SemanticAnalyse {

    //Stacks for Semantic values
    private Stack<String> typeStack =new Stack<String>();


    //syntax-directed definition
    public static treatRule(String s)
    {
        treatType(s);

    }

    private treatType (String s){
        switch (s){
            case  "Pro → D I" -> {
                String s1 = typeStack.pop();///only for I not type verification for D only syntax
                if(s1.equals(""))
                    typeStack.push("");
                else
                    typeStack.push("error");
            }
            case         
        }
    }
}
