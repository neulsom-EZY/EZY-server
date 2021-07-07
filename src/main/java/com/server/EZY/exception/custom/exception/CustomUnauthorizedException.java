package com.server.EZY.exception.custom.exception;

public class CustomUnauthorizedException extends RuntimeException{
    public CustomUnauthorizedException(String msg, Throwable t){
        super(msg, t);
    }
    public CustomUnauthorizedException(String msg){
        super(msg);
    }
    public CustomUnauthorizedException(){
        super();
    }
}
