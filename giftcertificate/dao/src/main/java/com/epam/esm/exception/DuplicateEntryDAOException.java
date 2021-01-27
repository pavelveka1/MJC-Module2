package com.epam.esm.exception;

/**
 * DuplicateEntryDAOException class.
 * This exception will be thrown when trying to insert a duplicate record into the database
 */
public class DuplicateEntryDAOException extends Exception {

    /**
     * Constructor with one parameter
     *
     * @param message description of problem
     */
    public DuplicateEntryDAOException(String message) {
        super(message);
    }

    /**
     * Constructor with two parameter
     *
     * @param message description of problem
     * @param cause   current exception
     */
    public DuplicateEntryDAOException(String message, Throwable cause) {
        super(message, cause);
    }

}
