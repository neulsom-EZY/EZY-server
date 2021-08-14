package com.server.EZY.exception.authentication_number.exception;

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
