package com.epam.esm.exception;

/**
 * RequestParamDAOException class.
 * This exception will be thrown when was passed incorrect parameters in request
 */
public class RequestParamDAOException extends Exception {

    /**
     * Constructor without parameters
     */
    public RequestParamDAOException() {
    }

    /**
     * Constructor with one parameter
     *
     * @param message description of problem
     */
    public RequestParamDAOException(String message) {
        super(message);
    }

    /**
     * Constructor with two parameter
     *
     * @param message description of problem
     * @param cause   current exception
     */
    public RequestParamDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
