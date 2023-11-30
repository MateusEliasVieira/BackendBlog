package com.seminfo.domain.domainException;

public class BusinessRulesException extends RuntimeException{
    public BusinessRulesException(String mensagem){
        super(mensagem);
    }
}
