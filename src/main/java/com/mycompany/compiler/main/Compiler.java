package com.mycompany.compiler.main;
import com.mycompany.compiler.lexical_ana.*;
import java.io.PushbackReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;




public class Compiler {


    public static void main(String[] args) throws FileNotFoundException {
    System.out.println("Donner votre fichier à compiler");
    Scanner s = new Scanner(System.in);
    String filePath="C:\\Users\\pc\\Documents\\NetBeansProjects\\Compiler\\src\\main\\ressources\\"+s.nextLine();
           

        try {
            FileReader fileReader = new FileReader(filePath);
            System.out.println("Fihcier chargé avec succés");
            System.out.println("----------------------------COMPILIING--------------------------------");
            
        try (PushbackReader b = new PushbackReader(fileReader)) {
            lexicalAnalyse ana =new lexicalAnalyse(b);
            ana.show_ularray();
            ana.show_lexems();
            ana.show_symbol();
            
            
            
            s.close();
        }
        } catch (FileNotFoundException e) {
            System.out.println("Attenetion ! erreur dans le fichier à compiler ");
        
            }
        catch (IOException e) {
    }
    }
}
