package com.server.EZY.exception.token.exception;

public class AuthorizationHeaderIsEmpty extends RuntimeException{
    public AuthorizationHeaderIsEmpty(String msg, Throwable t){
        super(msg, t);
    }
    public AuthorizationHeaderIsEmpty(String msg){
        super(msg);
    }
    public AuthorizationHeaderIsEmpty(){
        super();
    }
}
