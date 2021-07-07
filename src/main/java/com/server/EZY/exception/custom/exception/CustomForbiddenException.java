package com.server.EZY.exception.custom.exception;

public class CustomForbiddenException extends RuntimeException{
    public CustomForbiddenException(String msg, Throwable t){
        super(msg, t);
    }
    public CustomForbiddenException(String msg){
        super(msg);
    }
    public CustomForbiddenException(){
        super();
    }
}
