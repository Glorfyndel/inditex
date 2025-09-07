package com.example.inditex.application;

import com.example.inditex.domain.PriceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomRestExeptionHandler extends ResponseEntityExceptionHandler {

    public record ConstraintsErrorResponse(String message, Map<String, String> constraints) {
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

    @ExceptionHandler(PriceNotFoundException.class)
    public ProblemDetail handlePriceNotFoundException(PriceNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
