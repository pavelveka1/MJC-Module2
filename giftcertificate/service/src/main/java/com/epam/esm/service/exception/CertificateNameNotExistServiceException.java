package com.epam.esm.service.exception;

public class CertificateNameNotExistServiceException extends Exception {

    public CertificateNameNotExistServiceException() {
    }

    public CertificateNameNotExistServiceException(String message) {
        super(message);
    }

    public CertificateNameNotExistServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
