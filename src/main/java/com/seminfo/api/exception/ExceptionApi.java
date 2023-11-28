package com.seminfo.api.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExceptionApi {
    private String mensagem;
    private Date data;
}
