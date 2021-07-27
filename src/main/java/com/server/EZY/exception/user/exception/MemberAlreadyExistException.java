package com.server.EZY.exception.user.exception;

public class MemberAlreadyExistException extends RuntimeException{
    public MemberAlreadyExistException(String msg, Throwable t){
        super(msg, t);
    }
    public MemberAlreadyExistException(String msg){
        super(msg);
    }
    public MemberAlreadyExistException(){
        super();
    }
}
