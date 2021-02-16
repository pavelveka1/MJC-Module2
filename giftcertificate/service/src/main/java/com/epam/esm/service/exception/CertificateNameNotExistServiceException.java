package com.epam.esm.service.exception;

public class CertificateNameNotExistServiceException extends Exception {

    private String certificateName;

    public CertificateNameNotExistServiceException() {
    }

    public CertificateNameNotExistServiceException(String message) {
        super(message);
    }

    public CertificateNameNotExistServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificateNameNotExistServiceException(String message, String certificateName) {
        super(message);
        this.certificateName = certificateName;
    }

    public String getCertificateName() {
        return certificateName;
    }
}
