package com.epam.esm.exception;

/**
 * TagNameNotExistDAOException class.
 * This exception will be thrown when wore passed incorrect parameters in request
 */
public class TagNameNotExistDAOException extends Exception {

    /**
     * Constructor without parameters
     */
    public TagNameNotExistDAOException() {
    }

    /**
     * Constructor with one parameter
     *
     * @param message description of problem
     */
    public TagNameNotExistDAOException(String message) {
        super(message);
    }

    /**
     * Constructor with two parameter
     *
     * @param message description of problem
     * @param cause   current exception
     */
    public TagNameNotExistDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
