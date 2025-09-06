package com.example.inditex.application;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomRestExeptionHandler extends ResponseEntityExceptionHandler {

    public record ConstraintsErrorResponse(String message, Map<String, String> constraints) {
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        final Optional<ObjectError> optionalGobalError = Optional.ofNullable(ex.getBindingResult().getGlobalError());
        final String globalMessageError = optionalGobalError.map(ObjectError::getDefaultMessage).orElse("Validation error");

        final Map<String, String> constraints = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        final ConstraintsErrorResponse constraintsErrorResponse = new ConstraintsErrorResponse(globalMessageError, constraints);

        return new ResponseEntity<>(constraintsErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        final Map<String, String> constraints = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage()
                ));

        final ConstraintsErrorResponse constraintsErrorResponse = new ConstraintsErrorResponse("Validation error", constraints);

        return new ResponseEntity<>(constraintsErrorResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
//        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
//    }
}
