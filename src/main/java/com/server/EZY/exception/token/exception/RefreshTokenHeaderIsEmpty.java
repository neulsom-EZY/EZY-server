package com.server.EZY.exception.token.exception;

public class RefreshTokenHeaderIsEmpty extends RuntimeException{
    public RefreshTokenHeaderIsEmpty(String msg, Throwable t){
        super(msg, t);
    }
    public RefreshTokenHeaderIsEmpty(String msg){
        super(msg);
    }
    public RefreshTokenHeaderIsEmpty(){
        super();
    }
}
