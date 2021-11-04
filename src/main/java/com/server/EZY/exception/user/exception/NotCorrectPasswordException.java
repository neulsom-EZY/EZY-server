package com.server.EZY.exception.user.exception;

public class NotCorrectPasswordException extends RuntimeException {
    public NotCorrectPasswordException(String msg, Throwable t){
        super(msg, t);
    }
    public NotCorrectPasswordException(String msg){
        super(msg);
    }
    public NotCorrectPasswordException(){
        super();
    }
}
