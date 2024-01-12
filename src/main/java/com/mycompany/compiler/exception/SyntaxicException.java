package com.mycompany.compiler.exception;

public class SyntaxicException extends RuntimeException{
    public SyntaxicException(String statement){
        super(statement);
    }
}
