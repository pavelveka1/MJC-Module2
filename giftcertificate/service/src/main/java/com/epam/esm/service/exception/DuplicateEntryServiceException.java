package com.epam.esm.service.exception;

/**
 * DuplicateEntryServiceException class.
 * This exception will be thrown when trying to insert a duplicate record into the database
 */
public class DuplicateEntryServiceException extends Exception {

    private String language;

    /**
     * Constructor without parameters
     */
    public DuplicateEntryServiceException() {
    }

    /**
     * Constructor with one parameter
     *
     * @param message description of problem
     */
    public DuplicateEntryServiceException(String message) {
        super(message);
    }

    /**
     * Constructor with two parameter
     *
     * @param message description of problem
     * @param cause   current exception
     */
    public DuplicateEntryServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateEntryServiceException(String message, String language) {
        super(message);
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
