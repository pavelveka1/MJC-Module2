package com.epam.esm.exception;

/**
 * IdNotExistDAOException class.
 * This exception will be thrown when trying to get/delete data from database and this id is not exist in DB
 */
public class IdNotExistDAOException extends Exception {

    /**
     * Constructor with one parameter
     *
     * @param message description of problem
     */
    public IdNotExistDAOException(String message) {
        super(message);
    }

    /**
     * Constructor with two parameter
     *
     * @param message description of problem
     * @param cause   current exception
     */
    public IdNotExistDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
