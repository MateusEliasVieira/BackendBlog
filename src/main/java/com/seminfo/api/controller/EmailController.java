package com.seminfo.api.controller;

import com.seminfo.api.dto.others.Message;
import com.seminfo.domain.model.User;
import com.seminfo.domain.service.EmailSenderService;
import com.seminfo.domain.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/email")
public class EmailController
{

    @Autowired
    private UserService service;

    @Autowired
    private EmailSenderService emailSenderService;

    @GetMapping("/confirmation/{token}")
    public ResponseEntity<Message> confirmMyCountByReceiveEmail(@PathVariable("token") String token)
    {
        Message message = new Message();
        // Decodificando a string Base64
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String tokenDecodedString = new String(decodedBytes);
        User userConfirmed = service.saveUserAfterConfirmedAccountByEmail(tokenDecodedString);
        if(userConfirmed != null)
        {
            // saved
            message.setMessage("Account confirmed successfully!");
            return new ResponseEntity<Message>(message, HttpStatus.OK);
        }
        else
        {
            message.setMessage("Error when confirming account!");
            return new ResponseEntity<Message>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/recover-account/{email}")
    public ResponseEntity<String> recoverAccount(@PathVariable("email") @Email String email)
    {
        try
        {
            if(emailSenderService.recoverAccount(email))
            {
                return new ResponseEntity<String>("Account recovery email sent successfully!",HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<String>("There is no registered user with this email!",HttpStatus.NOT_ACCEPTABLE);
            }
        }
        catch (MessagingException e)
        {
            return new ResponseEntity<String>("Account recovery email could not be sent!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
