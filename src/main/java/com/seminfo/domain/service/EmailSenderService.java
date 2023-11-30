package com.seminfo.domain.service;

public interface EmailSenderService {
    public void sendEmail(String to, String token);
    public void recoverAccount(String to);

}
