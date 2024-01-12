package com.mycompany.compiler.exception;

public class SemanticException extends RuntimeException{
    public SemanticException(String statement){
        super(statement);
    }
}
