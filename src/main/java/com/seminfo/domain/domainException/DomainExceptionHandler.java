package com.seminfo.domain.domainException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;

@ControllerAdvice
public class DomainExceptionHandler {
    @ExceptionHandler(BusinessRulesException.class)
    public ResponseEntity<Problem> handleRegrasDeNegocioException(BusinessRulesException ex) {

        var status = HttpStatus.BAD_REQUEST;

        var problem = new Problem();
        problem.setStatus(status.value());
        problem.setTitle(ex.getMessage());
        problem.setDate(OffsetDateTime.now());

        return ResponseEntity.badRequest().body(problem);
    }
}
