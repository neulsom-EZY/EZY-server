package com.server.EZY.exception.custom.exception;

public class CustomNotFoundException extends RuntimeException{
    public CustomNotFoundException(String msg, Throwable t){
        super(msg, t);
    }
    public CustomNotFoundException(String msg){
        super(msg);
    }
    public CustomNotFoundException(){
        super();
    }
}