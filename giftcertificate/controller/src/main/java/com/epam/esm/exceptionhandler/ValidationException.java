package com.epam.esm.exceptionhandler;

public class ValidationException extends Exception {

    private String language;
    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String message, String language) {
        super(message);
        this.language=language;
    }

    public String getLanguage() {
        return language;
    }
}
