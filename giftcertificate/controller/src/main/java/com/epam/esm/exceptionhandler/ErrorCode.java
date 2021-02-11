package com.epam.esm.exceptionhandler;

public enum ErrorCode {

    BAD_REQUEST(40001),
    INTERNAL_SERVER_ERROR(50001),
    NOT_FOUND(40401);

    private int errorCode;
    ErrorCode(int errorCode){
        this.errorCode=errorCode;
    }

    public int getErrorCode(){
        return errorCode;
    }
}
