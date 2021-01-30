package com.epam.esm.service.exception;

public class TagNotExistServiceException extends Exception {

    public TagNotExistServiceException() {
    }

    public TagNotExistServiceException(String message) {
        super(message);
    }

    public TagNotExistServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagNotExistServiceException(Throwable cause) {
        super(cause);
    }
}
