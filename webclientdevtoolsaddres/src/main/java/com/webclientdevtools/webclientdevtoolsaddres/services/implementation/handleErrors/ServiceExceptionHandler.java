package com.webclientdevtools.webclientdevtoolsaddres.services.implementation.handleErrors;

import com.webclientdevtools.webclientdevtoolsaddres.exception.AddressNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ServiceExceptionHandler {
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<StandardError> UserNotFound(AddressNotFoundException e, HttpServletRequest request){
        StandardError error = new StandardError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Endereço não localizado na base de dados",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
