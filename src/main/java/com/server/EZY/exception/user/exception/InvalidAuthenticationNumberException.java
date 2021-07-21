package com.server.EZY.exception.user.exception;

public class InvalidAuthenticationNumberException extends RuntimeException{
    public InvalidAuthenticationNumberException(String msg, Throwable t){
        super(msg, t);
    }
    public InvalidAuthenticationNumberException(String msg){
        super(msg);
    }
    public InvalidAuthenticationNumberException(){
        super();
    }
}
