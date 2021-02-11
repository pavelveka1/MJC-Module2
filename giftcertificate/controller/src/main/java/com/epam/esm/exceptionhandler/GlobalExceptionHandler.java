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

    @ExceptionHandler
    public ResponseEntity<ErrorTO> unKnownException(Exception exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleIdNotExistServiceException(IdNotExistServiceException exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), ErrorCode.NOT_FOUND.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleDuplicateEntryServiceException(DuplicateEntryServiceException exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleRequestParamServiceException(RequestParamServiceException exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleTagNotExistServiceException(TagNotExistServiceException exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), ErrorCode.NOT_FOUND.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleValidationException(ValidationException exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), ErrorCode.BAD_REQUEST.getErrorCode()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorTO> handleUpdateException(UpdateServiceException exception) {
        return new ResponseEntity<>(new ErrorTO(exception.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
