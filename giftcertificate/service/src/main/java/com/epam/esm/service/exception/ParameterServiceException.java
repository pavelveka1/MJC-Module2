package com.epam.esm.service.exception;

public class ParameterServiceException extends Exception {

    public ParameterServiceException() {
    }

    public ParameterServiceException(String message) {
        super(message);
    }

    public ParameterServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
