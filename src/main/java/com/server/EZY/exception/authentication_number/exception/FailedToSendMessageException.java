package com.server.EZY.exception.authentication_number.exception;

public class FailedToSendMessageException extends RuntimeException{
    public FailedToSendMessageException(String msg, Throwable t){
        super(msg, t);
    }
    public FailedToSendMessageException(String msg){
        super(msg);
    }
    public FailedToSendMessageException(){
        super();
    }
}
