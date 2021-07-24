package com.server.EZY.exception.authenticationNumber.exception;

public class AuthenticationNumberTransferFailedException extends RuntimeException{
    public AuthenticationNumberTransferFailedException(String msg, Throwable t){
        super(msg, t);
    }
    public AuthenticationNumberTransferFailedException(String msg){
        super(msg);
    }
    public AuthenticationNumberTransferFailedException(){
        super();
    }
}
