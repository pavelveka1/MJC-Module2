package com.epam.esm.service.exception;

public class PaginationException extends Exception {

    private String language;

    public PaginationException() {
    }

    public PaginationException(String message) {
        super(message);
    }

    public PaginationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaginationException(String message, String language) {
        super(message);
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
