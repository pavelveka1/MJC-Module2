package com.epam.esm.exceptionhandler;

import com.epam.esm.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class GlobalExceptionHandler is used for catching all exceptions, which declared inside class
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MESSAGES = "messages";

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleIdNotExistServiceException(IdNotExistServiceException exception) {
        Locale locale = new Locale(exception.getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGES, locale);
        return new ResponseEntity<>(new ErrorTO(bundle.getString(exception.getMessage()), ErrorCode.NOT_FOUND.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleDuplicateEntryServiceException(DuplicateEntryServiceException exception) {
        Locale locale = new Locale(exception.getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGES, locale);
        return new ResponseEntity<>(new ErrorTO(bundle.getString(exception.getMessage()), ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleRequestParamServiceException(RequestParamServiceException exception) {
        Locale locale = new Locale(exception.getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGES, locale);
        return new ResponseEntity<>(new ErrorTO(bundle.getString(exception.getMessage()), ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleTagNotExistServiceException(TagNotExistServiceException exception) {
        Locale locale = new Locale(exception.getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGES, locale);
        return new ResponseEntity<>(new ErrorTO(bundle.getString(exception.getMessage()), ErrorCode.NOT_FOUND.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleValidationException(ValidationException exception) {
        Locale locale = new Locale(exception.getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGES, locale);
        return new ResponseEntity<>(new ErrorTO(bundle.getString(exception.getMessage()), ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleUpdateException(UpdateServiceException exception) {
        Locale locale = new Locale(exception.getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGES, locale);
        return new ResponseEntity<>(new ErrorTO(bundle.getString(exception.getMessage()), ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleParamException(ParameterServiceException exception) {
        Locale locale = new Locale(exception.getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGES, locale);
        return new ResponseEntity<>(new ErrorTO(bundle.getString(exception.getMessage()), ErrorCode.NOT_FOUND.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleCertificateNameNotExistException(CertificateNameNotExistServiceException exception) {
        Locale locale = new Locale(exception.getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGES, locale);
        return new ResponseEntity<>(new ErrorTO(bundle.getString(exception.getMessage())+" "+ exception.getCertificateName(),
                ErrorCode.NOT_FOUND.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleCertificateNameNotExistException(PaginationException exception) {
        Locale locale = new Locale(exception.getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle(MESSAGES, locale);
        return new ResponseEntity<>(new ErrorTO(bundle.getString(exception.getMessage()), ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorTO> unknownException(Exception exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }
}
