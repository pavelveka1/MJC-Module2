package com.epam.esm.service.exception;

public class CertificateNameNotExistServiceException extends Exception {

    private String language;
    private String certificateName;

    public CertificateNameNotExistServiceException() {
    }

    public CertificateNameNotExistServiceException(String message) {
        super(message);
    }

    public CertificateNameNotExistServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificateNameNotExistServiceException(String message, String language, String certificateName) {
        super(message);
        this.language = language;
        this.certificateName = certificateName;
    }

    public String getLanguage() {
        return language;
    }

    public String getCertificateName() {
        return certificateName;
    }
}
