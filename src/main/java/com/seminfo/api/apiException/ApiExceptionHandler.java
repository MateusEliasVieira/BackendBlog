package com.seminfo.api.apiException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {

    /*
        MethodArgumentNotValidException.
        Essa exceção é lançada pelo Spring quando a validação de
        um argumento(parâmetro) do método falha, e é o que acontece quando
        você usa a anotação @Valid em um parâmetro de método.
    */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<Field> list = new ArrayList<Field>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {

            Field field = new Field();
            field.setNameField(error.getField());
            field.setMessage(error.getDefaultMessage());

            list.add(field);

        });

        var status = HttpStatus.BAD_REQUEST;

        var problema = new Problem();
        problema.setTitle(com.seminfo.utils.Field.DEFAULT_MESSAGE_EXCEPTION);
        problema.setDate(OffsetDateTime.now());
        problema.setStatus(status.value());
        problema.setList(list);

        return ResponseEntity.badRequest().body(problema);
    }

}
