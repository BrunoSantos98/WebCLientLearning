package com.webclientdevtools.webclientdevtoolscustomer.services.implementation.handleErrors;

import com.webclientdevtools.webclientdevtoolscustomer.exception.UserExistsConflictException;
import com.webclientdevtools.webclientdevtoolscustomer.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ServiceExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandardError> UserNotFound(UserNotFoundException e, HttpServletRequest request){
        StandardError error = new StandardError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Usuario não localizado na base de dados através dos dados informados",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserExistsConflictException.class)
    public ResponseEntity<StandardError> UserConflictFound(UserExistsConflictException e, HttpServletRequest request){
        StandardError error = new StandardError(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Usuario informado ja existente na base de dados",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
