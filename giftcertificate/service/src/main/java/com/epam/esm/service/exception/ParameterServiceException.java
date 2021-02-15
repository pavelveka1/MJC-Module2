package com.epam.esm.service.exception;

public class ParameterServiceException extends Exception {

    private String language;

    public ParameterServiceException() {
    }

    public ParameterServiceException(String message) {
        super(message);
    }

    public ParameterServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterServiceException(String message, String language) {
        super(message);
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
