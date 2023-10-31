package com.seminfo.domain.service.impl;

import com.seminfo.domain.service.EmailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Value("${spring.mail.username}")
    private String MY_GMAIL;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to,String token) throws MailException, MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        // Define o destinatário, assunto e o conteúdo HTML do e-mail
        helper.setTo(to);
        helper.setSubject("Account confirmation");

        // Codificando a string para Base64
        byte[] encodedBytes = Base64.getEncoder().encode(token.getBytes());

        // Convertendo os bytes codificados de volta para uma string
        String tokenBase64String = new String(encodedBytes);

        // Use HTML para criar um link estilizado
        String htmlContent = "<p>Please click the link below to validate your email and create your account</p>" +
                             "<p><a href=\"http://localhost:5173/confirmation?token=" + tokenBase64String + "\" style=\"color: #007BFF; text-decoration: none;\">Confirmar!</a></p>";
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);

    }
}
