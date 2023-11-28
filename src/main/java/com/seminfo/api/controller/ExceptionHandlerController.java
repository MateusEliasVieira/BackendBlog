package com.seminfo.api.controller;


import com.seminfo.api.exception.ExceptionApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerController {

    /*
        MethodArgumentNotValidException.
        Essa exceção é lançada pelo Spring quando a validação de
        um argumento(parâmetro) do método falha, e é o que acontece quando
        você usa a anotação @Valid em um parâmetro de método.
    */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ExceptionApi>> handleValidationException(MethodArgumentNotValidException ex) {

        List<ExceptionApi> list = new ArrayList<ExceptionApi>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {

            ExceptionApi exceptionApi = new ExceptionApi();
            exceptionApi.setMensagem(error.getDefaultMessage());
            exceptionApi.setData(Date.from(Instant.now().atZone(ZoneId.of("America/Sao_Paulo")).toInstant()));

            list.add(exceptionApi);

        });
        return ResponseEntity.badRequest().body(list);
    }

}
