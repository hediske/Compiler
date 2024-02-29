
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
    private ArrayList<lexic_unit> ularray =new ArrayList<>();  //array of lexic unit  
    static
    {
        SymbolTable.put("while",new lexic_unit("while",0,"NONE"));        
        SymbolTable.put("if",new lexic_unit("if",0,"NONE"));
        SymbolTable.put("else",new lexic_unit("else",0,"NONE"));
        SymbolTable.put("char",new lexic_unit("char",0,"NONE"));        
        SymbolTable.put("string",new lexic_unit("string",0,"NONE"));        
        SymbolTable.put("bool",new lexic_unit("bool",0,"NONE"));        
        SymbolTable.put("int",new lexic_unit("int",0,"NONE"));        
        SymbolTable.put("function",new lexic_unit("function",0,"NONE"));        
        SymbolTable.put("true",new lexic_unit("true",0,"BOOL"));        
        SymbolTable.put("false",new lexic_unit("false",0,"BOOL"));        
        
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
    private Boolean is_operator_unary(char c){
        return c=='!' || c=='–';
    }
    private Boolean is_string(char c){
        return c=='"';


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
                     ularray.add(new lexic_unit("nb",s,"INT"));
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
                        case '+' -> ularray.add(new lexic_unit("opari","+","NONE"));
                        case '-' -> ularray.add(new lexic_unit("opari","-","NONE"));
                        case '*' -> ularray.add(new lexic_unit("opari","*","NONE"));
                        default -> ularray.add(new lexic_unit("opari","+","NONE"));
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
                                 ularray.add(new lexic_unit("oprel","==","NONE"));
                             else
                             {
                                 b.unread(character);
                                 ularray.add(new lexic_unit("=","=","NONE"));
                             }
                         }
                         else
                         {
                             ularray.add(new lexic_unit("oprel","=","NONE"));
                         }
                         
                     }
                    else if(c=='<')
                    {
                        character=b.read();
                         if(character!=-1)
                         {
                             c =(char)character;
                            switch (c) {
                                case '=' -> ularray.add(new lexic_unit("oprel","<=","NONE"));
                                case '>' -> ularray.add(new lexic_unit("oprel","<>","NONE"));
                                default -> {
                                    b.unread(character);
                                    ularray.add(new lexic_unit("oprel","<","NONE"));
                                }
                            }
                         }
                         else
                         {
                             ularray.add(new lexic_unit("oprel","<","NONE"));
                         }
                    } 
                    else if(c=='>')
                    {
                        character=b.read();
                         if(character!=-1)
                         {
                             c =(char)character;
                            switch (c) {
                                case '=' -> ularray.add(new lexic_unit("oprel",">=","NONE"));
                                default -> {
                                    b.unread(character);
                                    ularray.add(new lexic_unit("oprel",">","NONE"));
                                }
                            }
                         }
                         else
                         {
                            ularray.add(new lexic_unit("oprel",">","NONE"));
                         }
                    }                        

                     
                }
                else if (is_special_caracter(c)){
                switch(c){
                    case ';' -> ularray.add(new lexic_unit(";",";","NONE"));
                    case ',' -> ularray.add(new lexic_unit(",",",","NONE"));
                    case ':' -> ularray.add(new lexic_unit(":",":","NONE"));
                    case ')' -> ularray.add(new lexic_unit(")",")","NONE"));
                    case '(' -> ularray.add(new lexic_unit("(","(","NONE"));
                    case '{' -> ularray.add(new lexic_unit("}","}","NONE"));
                    default -> ularray.add(new lexic_unit("{","{","NONE"));
                }
                }
                else if (is_operator_unary(c)){
                    switch(c){
                        case '!' -> ularray.add(new lexic_unit("opnot","!","NONE"));
                        default -> ularray.add(new lexic_unit("opneg","–","NONE"));
                        }
                    }
                else if (is_string(c)){
                    StringBuilder sb = new StringBuilder();
                    sb.append("\"");  
                    while(character!=-1) {
                        character=b.read();
                        if(character!=-1 ){
                            c=(char) character;
                            if(c == '\r'  || c== '\n'){
                                throw new LexicalException("Attention !! An  error occured in line "+
                                String.valueOf(nbrrow) +
                                " \n expected \" for String statement");
                            }
                            else if(c!='"')
                                {
                                    sb.append(c);
                                }
                            else{
                                sb.append('"');
                                break;
                            }
                        }
                    }
                        if(character==-1)
                            throw new LexicalException("Attention !! An  error occured in line "+
                            String.valueOf(nbrrow) +
                            " \n expected \" for String statement");
                        else {
                            String s = sb.toString();
                            ularray.add(new lexic_unit("str",s,"STRING"));                        }

                }
                
                
                else if(is_operator_bool(c)){                 
                    character=b.read();
                    if((char)character!=c)
                    {
                        throw( new LexicalException("Attention !! An  error occured in line "+
                        String.valueOf(nbrrow) +
                        " \n caracter "+String.valueOf(c) +"is not valid"));
                    }
                    else{
                        switch (c) {
                            case '|' ->ularray.add(new lexic_unit("opbol","||","NONE"));                       
                            default  ->ularray.add(new lexic_unit("opbol","&&","NONE"));
                        }
                    }
                    
                }
                else{
                    throw( new LexicalException("Attention !! An  error occured in line "+
                    String.valueOf(nbrrow) +
                    " \n caracter "+String.valueOf(c) +"is not valid"));
                }
                }

                
                
                
             
                
                
                
                }
                 
                
                
                
                
            

    public Map <String , lexic_unit >  getLexems(){
        return this.lexemTable;
    }            
    public ArrayList<lexic_unit>  getUlarray(){
        return this.ularray;
    }            
    public  static Map <String , lexic_unit >  getSymbols(){
        return SymbolTable;
    }            
                


    public void show_ularray()
    {
        for (var u:ularray)
        {
            System.out.println("Lexic Unit  :  "+u.getUnilexid()+"  :  "+u.toString());       }
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
    public lexic_unit GetLexem(String key){
        if(lexemTable.keySet().contains(key))
            return lexemTable.get(key);
        else
            return null;
    }
    public String getKeyByRank(String rank){
        for(String s : lexemTable.keySet()){
            if(lexemTable.get(s).getRangerid().equals(rank))
                return s;
        }
        return null;
    }
    public void SetLexemType(String Type,String key){
        lexic_unit s = GetLexem(key);
        if(s!=null){
            s.setType(Type);
        }
    }

}