package com.mycompany.compiler.exception;

public class LexicalException extends RuntimeException{
    public LexicalException(String statement){
        super(statement);
    }
}
