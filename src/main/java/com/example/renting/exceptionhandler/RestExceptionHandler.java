package com.example.renting.exceptionhandler;

import com.example.renting.exception.RentalException;
import com.example.renting.model.BasicRestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors().get(0).getDefaultMessage();

        return ResponseEntity
                .badRequest()
                .body(BasicRestResponse.message(errorMessage));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {

        String errorMessage = new StringBuilder()
                .append(ex.getMessage())
                .append(". Supported method")
                .append(Objects.requireNonNull(ex.getSupportedMethods()).length > 1 ? "s are: " : " is: ")
                .append(String.join(", ", Objects.requireNonNull(ex.getSupportedMethods())))
                .append(".").toString();

        return ResponseEntity
                .badRequest()
                .body(BasicRestResponse.message(errorMessage));
    }

    @ExceptionHandler(RentalException.class)
    public ResponseEntity handleRentalException(RentalException ex) {

        return ResponseEntity
                .status(ex.getCode())
                .body(BasicRestResponse.message(ex.getMessage()));
    }
}
