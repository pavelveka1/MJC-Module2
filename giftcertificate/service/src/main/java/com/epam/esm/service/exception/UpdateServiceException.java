package com.epam.esm.service.exception;

public class UpdateServiceException extends Exception {

    private String language;

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

    public UpdateServiceException(String message, String language) {
        super(message);
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
