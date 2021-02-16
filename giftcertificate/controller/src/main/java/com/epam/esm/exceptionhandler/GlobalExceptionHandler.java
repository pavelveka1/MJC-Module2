package com.epam.esm.exceptionhandler;

import com.epam.esm.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleIdNotExistServiceException(IdNotExistServiceException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getMessage(), null, locale);
        return new ResponseEntity<>(new ErrorTO(errorMessage, ErrorCode.NOT_FOUND.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleDuplicateEntryServiceException(DuplicateEntryServiceException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getMessage(), null, locale);
        return new ResponseEntity<>(new ErrorTO(errorMessage, ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleRequestParamServiceException(RequestParamServiceException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getMessage(), null, locale);
        return new ResponseEntity<>(new ErrorTO(errorMessage, ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleTagNotExistServiceException(TagNotExistServiceException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getMessage(), null, locale);
        return new ResponseEntity<>(new ErrorTO(errorMessage, ErrorCode.NOT_FOUND.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleValidationException(ValidationException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getMessage(), null, locale);
        return new ResponseEntity<>(new ErrorTO(errorMessage, ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleUpdateException(UpdateServiceException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getMessage(), null, locale);
        return new ResponseEntity<>(new ErrorTO(errorMessage, ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleParamException(ParameterServiceException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getMessage(), null, locale);
        return new ResponseEntity<>(new ErrorTO(errorMessage, ErrorCode.NOT_FOUND.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleCertificateNameNotExistException(CertificateNameNotExistServiceException exception,
                                                                          Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getMessage(), null, locale);
        return new ResponseEntity<>(new ErrorTO(errorMessage + " " + exception.getCertificateName(),
                ErrorCode.NOT_FOUND.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleCertificateNameNotExistException(PaginationException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getMessage(), null, locale);
        return new ResponseEntity<>(new ErrorTO(errorMessage, ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorTO> unknownException(Exception exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }
}
