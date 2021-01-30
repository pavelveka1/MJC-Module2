package com.epam.esm.exceptionhandler;

import com.epam.esm.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Class GlobalExceptionHandler is used for catching all exceptions, which declared inside class
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final int NOT_FOUND = 404;
    private static final int BAD_REQUEST = 400;
    private static final int INTERNAL_SERVER_ERROR = 500;

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleIdNotExistServiceException(IdNotExistServiceException exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleDuplicateEntryServiceException(DuplicateEntryServiceException exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleRequestParamServiceException(RequestParamServiceException exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleTagNotExistServiceException(TagNotExistServiceException exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleValidationException(ValidationException exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleUpdateException(UpdateServiceException exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
