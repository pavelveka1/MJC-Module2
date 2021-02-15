package com.epam.esm.service.exception;

/**
 * TagNameNotExistServiceException class.
 * This exception will be thrown when was passed incorrect parameters in request
 */
public class TagNameNotExistServiceException extends Exception {

    private String language;

    /**
     * Constructor without parameters
     */
    public TagNameNotExistServiceException() {
    }

    /**
     * Constructor with one parameter
     *
     * @param message description of problem
     */
    public TagNameNotExistServiceException(String message) {
        super(message);
    }

    /**
     * Constructor with two parameter
     *
     * @param message description of problem
     * @param cause   current exception
     */
    public TagNameNotExistServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagNameNotExistServiceException(String message, String language) {
        super(message);
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
