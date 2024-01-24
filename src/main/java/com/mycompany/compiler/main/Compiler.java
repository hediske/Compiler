package com.mycompany.compiler.main;
import com.mycompany.compiler.exception.*;
import com.mycompany.compiler.lexical_ana.*;
import java.io.PushbackReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import com.mycompany.compiler.syntax_ana.*;;



public class Compiler {


    public static void main(String[] args) throws FileNotFoundException {
    System.out.println("Donner votre fichier à compiler");
    Scanner s = new Scanner(System.in);
    String file =s.nextLine();
    String filePath="src\\main\\ressources\\"+file;
           

        try {
            FileReader fileReader = new FileReader(filePath);
            System.out.println("Fichier "+file+" chargé avec succés");
            System.out.println("----------------------------COMPILIING--------------------------------");
            
        try (PushbackReader b = new PushbackReader(fileReader)) {
            lexicalAnalyse ana =new lexicalAnalyse(b);
            ana.show_ularray();
            ana.show_lexems();
            ana.show_symbol();
            SyntaxAnalyse sn = new SyntaxAnalyse();
            sn.CodeSyntaxAnalyze(ana);
            s.close();  
        }
        } catch (FileNotFoundException e) {
            System.out.println("Attenetion ! erreur dans le fichier à compiler ");
        
            }
        catch (LexicalException | IOException e) {
            System.out.println(e.getMessage());
    }
    }
}
