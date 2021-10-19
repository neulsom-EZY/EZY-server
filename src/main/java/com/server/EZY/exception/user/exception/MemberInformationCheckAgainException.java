package com.server.EZY.exception.user.exception;

public class MemberInformationCheckAgainException extends RuntimeException {
    public MemberInformationCheckAgainException(String msg, Throwable t){
        super(msg, t);
    }
    public MemberInformationCheckAgainException(String msg){
        super(msg);
    }
    public MemberInformationCheckAgainException(){
        super();
    }
}
