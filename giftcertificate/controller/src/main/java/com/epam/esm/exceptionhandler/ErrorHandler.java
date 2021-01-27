package com.epam.esm.exceptionhandler;

/**
 * @Class ErrorHandler is used for represent error message and error code
 */
public class ErrorHandler {

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
     * @param errorCode code of exception
     */
    public ErrorHandler(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    /**
     * @return error message as String
     * @method getErrorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return error code as int
     * @method getErrorCode
     */
    public int getErrorCode() {
        return errorCode;
    }
}
