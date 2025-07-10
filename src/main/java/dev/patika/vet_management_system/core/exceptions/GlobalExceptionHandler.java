package dev.patika.vet_management_system.core.exceptions;

import dev.patika.vet_management_system.core.utils.Result;
import dev.patika.vet_management_system.core.utils.ResultData;
import dev.patika.vet_management_system.core.utils.ResultHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice // Indicates that this class provides global exception handling across all controllers
public class GlobalExceptionHandler {

    // Handles NotFoundException and returns a NOT_FOUND (404) response
    @ExceptionHandler
    public ResponseEntity<Result> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ResultHelper.notFoundError(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    // Handles validation errors and returns a BAD_REQUEST (400) response with validation messages
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultData<List<String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        // Extract validation error messages from the exception
        List<String> validationErrorList = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage).toList();
        // Return the validation errors in a standardized response
        return new ResponseEntity<>(ResultHelper.validateError(validationErrorList), HttpStatus.BAD_REQUEST);
    }

    // Handles IllegalArgumentException and returns a BAD_REQUEST (400) response
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ResultHelper.illegalArgument(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
