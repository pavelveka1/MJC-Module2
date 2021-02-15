package com.epam.esm.service.exception;

public class TagNotExistServiceException extends Exception {

    private String language;

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

    public TagNotExistServiceException(String message, String language) {
        super(message);
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
