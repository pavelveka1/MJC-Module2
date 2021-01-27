package com.epam.esm.service.exception;

/**
 * IdNotExistServiceException class.
 * This exception will be thrown when trying to get/delete data from database and this id is not exist in DB
 */
public class IdNotExistServiceException extends Exception {

    /**
     * Constructor without parameters
     */
    public IdNotExistServiceException() {
    }

    /**
     * Constructor with one parameter
     *
     * @param message description of problem
     */
    public IdNotExistServiceException(String message) {
        super(message);
    }

    /**
     * Constructor with two parameter
     *
     * @param message description of problem
     * @param cause   current exception
     */
    public IdNotExistServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with one parameter
     *
     * @param cause
     */
    public IdNotExistServiceException(Throwable cause) {
        super(cause);
    }
}
