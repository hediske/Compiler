
package com.mycompany.compiler.lexical_ana;
import com.mycompany.compiler.exception.*;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class lexicalAnalyse {
    private static final Map <String , lexic_unit > SymbolTable = new HashMap<>();
    private Map <String , lexic_unit > lexemTable = new HashMap<>();
    private ArrayList<lexic_unit> ularray =new ArrayList<>();    
    static
    {
        SymbolTable.put("if",new lexic_unit("if",0,"null"));
        SymbolTable.put("else",new lexic_unit("else",0,"null"));
        SymbolTable.put("while",new lexic_unit("while",0,"null"));        
        
    }

    public lexicalAnalyse() {
    }
    
    
    
    
    private Boolean is_delimiter(char c)
    {   
       return c ==' ' || c == '\r'  || c== '\n';
    }    
    private Boolean is_number(char c)
    {
       return (c >= '0' && c <= '9') ;
    }
    private Boolean is_letter(char c)
    {
        c=Character.toUpperCase(c);
        return(c>='A' && c<='Z');
    }
    private Boolean is_operator_arith(char c)
    {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    private Boolean is_operator_relat(char c)
    {
        return( c== '=' || c=='>' || c=='<');
    }
    private Boolean is_special_caracter(char c)
    {
        return(c==':' || c==',' || c==';' ||  c=='"' || c=='?' || c == '(' || c=='}' || c=='{' || c==')');
    }
    private Boolean is_operator_bool(char c){
        return c=='|' || c=='&';
    }

    public lexicalAnalyse(PushbackReader b) throws IOException {
            int character;
            int nbrrow=1;
            while ((character = b.read()) != -1) {
                char c = (char) character;
                
                
                if( is_delimiter(c))
                {   
                    if(c=='\n')
                    nbrrow++;
                    continue;
                }
                
                
                
                else if(is_number(c)) 
                {
                     StringBuilder sb = new StringBuilder();
                     sb.append(c);
                     while(  character!=-1    )
                     {
                        character = b.read();
                        if(character!=-1)
                        {
                            c = (char) character;
                            if(is_number(c))
                            {
                                sb.append(c);
                            }
                            else
                            {
                                b.unread(character);
                                break;
                            }
                        }
                        else {
                            break;
                        }
                     }
                     String s = sb.toString();
                     ularray.add(new lexic_unit("Number",s,"INT"));
                }
                     
                else if (is_letter(c))   
                {
                    StringBuilder sb = new StringBuilder();
                     sb.append(c);
                     while(  character!=-1    )
                     {
                        character = b.read();
                        if(character!=-1)
                        {
                            c = (char) character;
                            if(is_letter(c) || is_number(c))
                            {
                                sb.append(c);
                            }
                            else
                            {
                                b.unread(character);
                                break;
                            }
                        }
                        else {
                            break;
                        }
                     }
                     String s = (sb.toString()).toLowerCase();
                     
                     if ( SymbolTable.containsKey(s) )
                     {
                         ularray.add(SymbolTable.get(s));
                     }
                     else if(lexemTable.containsKey(s))
                     {
                         ularray.add(lexemTable.get(s));
                     }
                     else
                     {  
                         int rank=lexemTable.size()+1;
                         lexemTable.put(s,new lexic_unit("id",rank,"NONE"));
                         ularray.add(lexemTable.get(s));
                     }
                }
                
                else if(is_operator_arith(c))
                {
                    switch (c) {
                        case '+' -> ularray.add(new lexic_unit("add","+","NONE"));
                        case '-' -> ularray.add(new lexic_unit("sous","-","NONE"));
                        case '*' -> ularray.add(new lexic_unit("mult","*","NONE"));
                        default -> ularray.add(new lexic_unit("div","+","NONE"));
                    }
                }
                
                else if(is_operator_relat(c))
                {
                     if(c== '=')
                     {
                         character=b.read();
                         if(character!=-1)
                         {
                             c =(char)character;
                             if(c=='=')
                                 ularray.add(new lexic_unit("equ","==","NONE"));
                             else
                             {
                                 b.unread(character);
                                 ularray.add(new lexic_unit("aff","=","NONE"));
                             }
                         }
                         else
                         {
                             ularray.add(new lexic_unit("aff","=","NONE"));
                         }
                         
                     }
                    else if(c=='<')
                    {
                        character=b.read();
                         if(character!=-1)
                         {
                             c =(char)character;
                            switch (c) {
                                case '=' -> ularray.add(new lexic_unit("mse","<=","NONE"));
                                case '>' -> ularray.add(new lexic_unit("dif","<>","NONE"));
                                default -> {
                                    b.unread(character);
                                    ularray.add(new lexic_unit("mst","<","NONE"));
                                }
                            }
                         }
                         else
                         {
                             ularray.add(new lexic_unit("mst","<","NONE"));
                         }
                    } 
                    else if(c=='>')
                    {
                        character=b.read();
                         if(character!=-1)
                         {
                             c =(char)character;
                            switch (c) {
                                case '=' -> ularray.add(new lexic_unit("mbe",">=","NONE"));
                                default -> {
                                    b.unread(character);
                                    ularray.add(new lexic_unit("mbt",">","NONE"));
                                }
                            }
                         }
                         else
                         {
                            ularray.add(new lexic_unit("mbt",">","NONE"));
                         }
                    }                        

                     
                }
                else if (is_special_caracter(c)){
                switch(c){
                    case '"' -> ularray.add(new lexic_unit("delimstr",String.valueOf('"'),"NONE"));
                    case ';' -> ularray.add(new lexic_unit("brpoi",";","NONE"));
                    case ',' -> ularray.add(new lexic_unit("comma",",","NONE"));
                    case '?' -> ularray.add(new lexic_unit("intg","?","NONE"));
                    case ':' -> ularray.add(new lexic_unit("dblp",":","NONE"));
                    case ')' -> ularray.add(new lexic_unit("clopar",")","NONE"));
                    case '(' -> ularray.add(new lexic_unit("oppar","(","NONE"));
                    case '{' -> ularray.add(new lexic_unit("cloac","}","NONE"));
                    default -> ularray.add(new lexic_unit("oppac","{","NONE"));
                }
                }
                
                
                
                else if(is_operator_bool(c)){
                    if(c=='!')
                        ularray.add(new lexic_unit("nologic","!","NONE")); 
                    else
                    {                    
                    character=b.read();
                    if((char)character!=c)
                    {
                        throw( new LexicalException("Attention !! An  error occured in line "+
                        String.valueOf(nbrrow) +
                        " \n caracter "+String.valueOf(c) +"is not valid"));
                    }
                    else{
                        switch (c) {
                            case '|' ->ularray.add(new lexic_unit("orlogic","||","NONE"));                       
                            default  ->ularray.add(new lexic_unit("andlogic","&&","NONE"));
                        }
                    }}
                    
                }
                else{
                    throw( new LexicalException("Attention !! An  error occured in line "+
                    String.valueOf(nbrrow) +
                    " \n caracter "+String.valueOf(c) +"is not valid"));
                }
                }

                
                
                
             
                
                
                
                }
                 
                
                
                
                
            

                
                


    public void show_ularray()
    {
        for (var u:ularray)
        {
            System.out.println(u);       }
    }
    
    
    public void show_lexems ()
    {   int i=0;
        System.out.println("Lexems");
        for (var entry : lexemTable.entrySet()) {
            i++;
            System.out.println("Lexem "+ i  +"  :   "+ entry.getKey() + " : " + entry.getValue());
        }
        
    }

    public void show_symbol ()
    {   int i=0;
        System.out.println("Symbols");
        for (var entry : SymbolTable.entrySet()) {
            i++;
            System.out.println("Symbol "+ i +"  :   "+ entry.getKey() + " : " + entry.getValue());
        }
    }    
    
}