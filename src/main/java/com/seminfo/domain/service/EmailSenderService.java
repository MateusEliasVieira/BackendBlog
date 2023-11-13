package com.seminfo.domain.service;

import jakarta.mail.MessagingException;
import org.springframework.mail.MailException;

public interface EmailSenderService {
    public void sendEmail(String to, String token) throws MailException,MessagingException;
    public boolean recoverAccount(String to) throws MailException, MessagingException;


}
