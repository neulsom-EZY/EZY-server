package com.server.EZY.exception.token.exception;

public class TokenLoggedOutException extends RuntimeException{
    public TokenLoggedOutException(String msg, Throwable t){
        super(msg, t);
    }
    public TokenLoggedOutException(String msg){
        super(msg);
    }
    public TokenLoggedOutException(){
        super();
    }
}
