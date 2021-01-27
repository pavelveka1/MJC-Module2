package com.epam.esm.service.exception;

/**
 * RequestParamDAOException class.
 * This exception will be thrown when was passed incorrect parameters in request
 */
public class RequestParamServiceException extends Exception {

    /**
     * Constructor without parameters
     */
    public RequestParamServiceException() {
    }

    /**
     * Constructor with one parameter
     *
     * @param message description of problem
     */
    public RequestParamServiceException(String message) {
        super(message);
    }

    /**
     * Constructor with two parameter
     *
     * @param message description of problem
     * @param cause   current exception
     */
    public RequestParamServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
