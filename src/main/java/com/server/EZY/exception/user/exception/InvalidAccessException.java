package com.server.EZY.exception.user.exception;

public class InvalidAccessException extends RuntimeException{
    public InvalidAccessException(String msg, Throwable t){
        super(msg, t);
    }
    public InvalidAccessException(String msg){
        super(msg);
    }
    public InvalidAccessException(){
        super();
    }
}
