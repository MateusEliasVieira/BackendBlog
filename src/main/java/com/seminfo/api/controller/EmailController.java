package com.seminfo.api.controller;

import com.seminfo.api.model.EmailInput;
import com.seminfo.api.model.EmailOutput;
import com.seminfo.api.model.Message;
import com.seminfo.domain.service.EmailSenderService;
import com.seminfo.domain.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserService service;

    @GetMapping("/confirmation/{token}")
    public ResponseEntity<Message> confirmationEmailForNewAccount(@PathVariable String token){
        System.out.println("Chegou o token do email = "+token);
        Message message = new Message();
        HttpStatus httpStatus = null;
        if(service.confirmAccount(token)){
            message.setMessage("Account confirmed successfully!");
            httpStatus = HttpStatus.OK;
        }else{
            message.setMessage("Error when confirming account!");
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<Message>(message, httpStatus);

    }

}
