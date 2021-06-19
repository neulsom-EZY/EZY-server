package com.server.EZY.exception;

public class AccessTokenExpiredException extends RuntimeException{
    public AccessTokenExpiredException(String msg, Throwable t){
        super(msg, t);
    }
    public AccessTokenExpiredException(String msg){
        super(msg);
    }
    public AccessTokenExpiredException(){
        super();
    }

}