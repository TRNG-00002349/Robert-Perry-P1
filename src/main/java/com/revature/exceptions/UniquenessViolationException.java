package com.revature.exceptions;

public class UniquenessViolationException extends Exception{

    public UniquenessViolationException(String msg){
        super(msg);
    }
}