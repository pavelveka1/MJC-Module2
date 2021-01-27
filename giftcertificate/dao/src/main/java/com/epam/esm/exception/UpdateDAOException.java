package com.epam.esm.exception;

/**
 * UpdateDAOException class.
 * This exception will be thrown when object has not been updated in DB
 */
public class UpdateDAOException extends Exception {

    /**
     * Constructor without parameters
     */
    public UpdateDAOException() {
    }

    /**
     * Constructor with one parameter
     *
     * @param message description of problem
     */
    public UpdateDAOException(String message) {
        super(message);
    }

    /**
     * Constructor with two parameter
     *
     * @param message description of problem
     * @param cause   current exception
     */
    public UpdateDAOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with patameter Throwable
     * @param cause passed Throwable
     */
    public UpdateDAOException(Throwable cause) {
        super(cause);
    }
}
