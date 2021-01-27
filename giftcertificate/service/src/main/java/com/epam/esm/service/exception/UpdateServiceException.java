package com.epam.esm.service.exception;

public class UpdateServiceException extends Exception {

    public UpdateServiceException() {
    }

    public UpdateServiceException(String message) {
        super(message);
    }

    public UpdateServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateServiceException(Throwable cause) {
        super(cause);
    }
}
