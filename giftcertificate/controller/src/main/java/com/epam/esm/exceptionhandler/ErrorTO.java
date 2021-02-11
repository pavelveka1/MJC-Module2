package com.epam.esm.exceptionhandler;

/**
 * Class ErrorHandler is used for represent error message and error code
 */
public class ErrorTO {

    /**
     * Field contains error message
     */
    private final String errorMessage;

    /**
     * Field contains error code
     */
    private final int errorCode;

    /**
     * Constructor with two parameters
     *
     * @param errorMessage message about exception
     * @param errorCode    code of exception
     */
    public ErrorTO(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    /**
     * Method getErrorMessage
     *
     * @return error message as String
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Method getErrorCode
     *
     * @return error code as int
     */
    public int getErrorCode() {
        return errorCode;
    }
}
