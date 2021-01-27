package com.epam.esm.exceptionhandler;

import com.epam.esm.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Class GlobalExceptionHandler is used for catching all exceptions, which declared inside class
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorHandler> handleIdNotExistServiceException(IdNotExistServiceException exception) {
        return new ResponseEntity<ErrorHandler>(new ErrorHandler(exception.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorHandler> handleDuplicateEntryServiceException(DuplicateEntryServiceException exception) {
        return new ResponseEntity<ErrorHandler>(new ErrorHandler(exception.getMessage(), 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorHandler> handleRequestParamServiceException(RequestParamServiceException exception) {
        return new ResponseEntity<ErrorHandler>(new ErrorHandler(exception.getMessage(), 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorHandler> handleTagNameNotExistServiceException(TagNameNotExistServiceException exception) {
        return new ResponseEntity<ErrorHandler>(new ErrorHandler(exception.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorHandler> handleValidationException(ValidationException exception) {
        return new ResponseEntity<ErrorHandler>(new ErrorHandler(exception.getMessage(), 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorHandler> handleUpdateException(UpdateServiceException exception) {
        return new ResponseEntity<ErrorHandler>(new ErrorHandler(exception.getMessage(), 400), HttpStatus.BAD_REQUEST);
    }

}
